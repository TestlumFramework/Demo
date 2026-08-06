#!/usr/bin/env bash

set -euo pipefail

echo "Waiting for device + full boot..."
adb wait-for-device
adb shell 'while [ "$(getprop sys.boot_completed)" != "1" ]; do sleep 2; done'

echo "Installing APK: ${APK_PATH}"
adb install -r -g "${APK_PATH}"

echo "Starting Appium on host (port 4723)..."
nohup appium -p 4723 --allow-insecure="*:chromedriver_autodownload" > appium.log 2>&1 &

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

echo "Running Testlum..."
set +e
docker run -t -u "$(id -u):$(id -g)" --rm \
  --network=e2e_network \
  --add-host=host.docker.internal:host-gateway \
  -v "${PWD}/REGRESSION:/REGRESSION" \
  "${TESTLUM_IMAGE}" \
  -c=config-ci-regression-mobile-emulator.xml \
  -p=/REGRESSION/resources
EXIT_CODE=$?
set -e

echo "Testlum finished with exit code ${EXIT_CODE}"
case "${EXIT_CODE}" in
  0) echo "BUILD STATUS [SUCCESS]" ;;
  1) echo "BUILD STATUS [FAILURE] - Tests failed"; exit 1 ;;
  2) echo "BUILD STATUS [FAILURE] - No tests found"; exit 1 ;;
  *) echo "BUILD STATUS [FAILURE] - Invalid configuration or crash (Code: ${EXIT_CODE})"; exit 1 ;;
esac
