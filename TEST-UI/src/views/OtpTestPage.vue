<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gray-200">
    <div class="w-full max-w-sm p-6 bg-white rounded-md shadow-md">
      <h2 class="text-2xl font-semibold text-center text-gray-700">2FA Test Page</h2>
      <p class="mt-2 text-sm text-center text-gray-600">Enter your Google Authenticator code</p>

      <form class="mt-4" @submit.prevent="verifyOtp">
        <label class="block mt-3">
          <span class="text-sm text-gray-700">OTP Code</span>
          <input
            type="text"
            class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500"
            v-model="otpCode"
            placeholder="6-digit code"
          />
        </label>

        <div class="mt-6">
          <button
            type="submit"
            class="w-full px-4 py-2 text-sm text-center text-white bg-indigo-600 rounded-md focus:outline-none hover:bg-indigo-500"
          >
            Verify
          </button>
        </div>
      </form>
      
      <div v-if="message" :class="['mt-4 text-center font-bold', isSuccess ? 'text-green-600' : 'text-red-600']">
        <span id="resultMessage">{{ message }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import * as OTPAuth from "otpauth";

const otpCode = ref("");
const message = ref("");
const isSuccess = ref(false);

const SECRET = "JBSWY3DPEHPK3PXP";

function verifyOtp() {
  let totp = new OTPAuth.TOTP({
    issuer: "Demo",
    label: "user@demo.com",
    algorithm: "SHA1",
    digits: 6,
    period: 30,
    secret: OTPAuth.Secret.fromBase32(SECRET)
  });

  let delta = totp.validate({ token: otpCode.value, window: 1 });

  if (delta !== null) {
    message.value = "Success!";
    isSuccess.value = true;
  } else {
    message.value = "Invalid Code!";
    isSuccess.value = false;
  }
}
</script>
