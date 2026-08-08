#!/usr/bin/env bash

set -euo pipefail

CONFIG_FILE="${CONFIG_FILE:-config-ci-regression-mobile-emulator.xml}"
SUITE="${SUITE:-all}"

echo "Suite: ${SUITE} | Config: ${CONFIG_FILE}"

# Always collect diagnostics, however we exit. Without this a hung session
# leaves no trace: appium.log used to be dumped only if Appium failed to start.
# Every adb call here is wrapped in `timeout` on purpose: the failure mode we are
# diagnosing is a wedged adbd, and an unbounded adb call in the trap would hang
# the job for the rest of its timeout budget.
collect_diagnostics() {
  echo "Collecting diagnostics..."
  kill "${WATCHDOG_PID:-}" 2>/dev/null || true
  timeout 60 adb logcat -d > logcat.txt 2>/dev/null || true
  timeout 30 adb devices -l > adb-devices.txt 2>&1 || true
}
trap collect_diagnostics EXIT

echo "Waiting for device + full boot..."
adb wait-for-device
adb shell 'while [ "$(getprop sys.boot_completed)" != "1" ]; do sleep 2; done'

echo "Installing APK: ${APK_PATH}"
adb install -r -g "${APK_PATH}"

# A live Chrome process publishes a WEBVIEW_chrome context that the native
# webView commands can latch onto instead of the app's own webview, which
# hangs chromedriver attachment and wedges adbd.
echo "Force-stopping Chrome so it cannot publish a WEBVIEW_chrome context..."
adb shell am force-stop com.android.chrome || true

echo "Starting Appium on host (port 4723)..."
# Testlum's schema (REGRESSION/schema/native-config.xsd) only allows
# deviceName/platformVersion/udid/appPackage/appActivity in <appiumCapabilities>,
# so every timeout below can only be set here. Client-supplied capabilities still
# win, so these never conflict with what Testlum sends.
nohup appium -p 4723 \
  --log-timestamp \
  --allow-insecure="*:chromedriver_autodownload" \
  --default-capabilities '{
    "appium:adbExecTimeout": 120000,
    "appium:ignoreHiddenApiPolicyError": true,
    "appium:uiautomator2ServerLaunchTimeout": 120000,
    "appium:uiautomator2ServerInstallTimeout": 120000,
    "appium:androidInstallTimeout": 180000,
    "appium:suppressKillServer": true,
    "appium:skipLogcatCapture": true,
    "appium:disableWindowAnimation": true,
    "appium:enforceAppInstall": false
  }' > appium.log 2>&1 &

echo "Waiting for Appium /status..."
for i in $(seq 1 30); do
  if curl -sf http://localhost:4723/status > /dev/null; then
    echo "Appium is up"
    break
  fi
  sleep 2
  if [ "$i" -eq 30 ]; then
    echo "::error::Appium did not start"
    cat appium.log
    exit 1
  fi
done

# Record when adbd stops answering, so a wedged emulator is visible in the
# artifacts instead of only showing up as a wall of SessionNotCreated errors.
(
  while true; do
    printf '%s adb=%s\n' "$(date -u +%H:%M:%S)" "$(timeout 20 adb get-state 2>&1 || echo unreachable)"
    sleep 60
  done
) > adb-health.log 2>&1 &
WATCHDOG_PID=$!

echo "Running Testlum..."
set +e
docker run -t -u "$(id -u):$(id -g)" --rm \
  --network=e2e_network \
  --add-host=host.docker.internal:host-gateway \
  -v "${PWD}/REGRESSION:/REGRESSION" \
  "${TESTLUM_IMAGE}" \
  -c="${CONFIG_FILE}" \
  -p=/REGRESSION/resources
EXIT_CODE=$?
set -e

kill "${WATCHDOG_PID}" 2>/dev/null || true

echo "Testlum finished with exit code ${EXIT_CODE}"
case "${EXIT_CODE}" in
  0) echo "BUILD STATUS [SUCCESS]" ;;
  1) echo "BUILD STATUS [FAILURE] - Tests failed"; exit 1 ;;
  2) echo "BUILD STATUS [FAILURE] - No tests found"; exit 1 ;;
  *) echo "BUILD STATUS [FAILURE] - Invalid configuration or crash (Code: ${EXIT_CODE})"; exit 1 ;;
esac
