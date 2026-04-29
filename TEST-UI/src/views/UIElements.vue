<template>
  <div>
    <h3 class="text-3xl font-medium text-gray-700">UI Elements</h3>

    <!-- Alerts -->
    <div class="mt-4">
      <h4 class="text-gray-600">Alerts</h4>

      <div class="mt-4">
        <div
            class="px-4 py-4 overflow-x-auto bg-white rounded-md whitespace-nowrap"
        >
          <div
              class="inline-flex w-full max-w-sm ml-3 overflow-hidden bg-white rounded-lg shadow-md"
          >
            <div class="flex items-center justify-center w-12 bg-green-500">
              <svg
                  class="w-6 h-6 text-white fill-current"
                  viewBox="0 0 40 40"
                  xmlns="http://www.w3.org/2000/svg"
              >
                <path
                    d="M20 3.33331C10.8 3.33331 3.33337 10.8 3.33337 20C3.33337 29.2 10.8 36.6666 20 36.6666C29.2 36.6666 36.6667 29.2 36.6667 20C36.6667 10.8 29.2 3.33331 20 3.33331ZM16.6667 28.3333L8.33337 20L10.6834 17.65L16.6667 23.6166L29.3167 10.9666L31.6667 13.3333L16.6667 28.3333Z"
                />
              </svg>
            </div>

            <div class="px-4 py-2 -mx-3">
              <div class="mx-3">
                <span class="font-semibold text-green-500">Success</span>
                <p class="text-sm text-gray-600">
                  Your account was registered!
                </p>
              </div>
            </div>
          </div>

          <div
              class="inline-flex w-full max-w-sm ml-3 overflow-hidden bg-white rounded-lg shadow-md"
          >
            <div class="flex items-center justify-center w-12 bg-blue-500">
              <svg
                  class="w-6 h-6 text-white fill-current"
                  viewBox="0 0 40 40"
                  xmlns="http://www.w3.org/2000/svg"
              >
                <path
                    d="M20 3.33331C10.8 3.33331 3.33337 10.8 3.33337 20C3.33337 29.2 10.8 36.6666 20 36.6666C29.2 36.6666 36.6667 29.2 36.6667 20C36.6667 10.8 29.2 3.33331 20 3.33331ZM21.6667 28.3333H18.3334V25H21.6667V28.3333ZM21.6667 21.6666H18.3334V11.6666H21.6667V21.6666Z"
                />
              </svg>
            </div>

            <div class="px-4 py-2 -mx-3">
              <div class="mx-3">
                <span class="font-semibold text-blue-500">Info</span>
                <p class="text-sm text-gray-600">
                  Channel archived by the owner.
                </p>
              </div>
            </div>
          </div>

          <div
              class="inline-flex w-full max-w-sm ml-3 overflow-hidden bg-white rounded-lg shadow-md"
          >
            <div class="flex items-center justify-center w-12 bg-yellow-500">
              <svg
                  class="w-6 h-6 text-white fill-current"
                  viewBox="0 0 40 40"
                  xmlns="http://www.w3.org/2000/svg"
              >
                <path
                    d="M20 3.33331C10.8 3.33331 3.33337 10.8 3.33337 20C3.33337 29.2 10.8 36.6666 20 36.6666C29.2 36.6666 36.6667 29.2 36.6667 20C36.6667 10.8 29.2 3.33331 20 3.33331ZM21.6667 28.3333H18.3334V25H21.6667V28.3333ZM21.6667 21.6666H18.3334V11.6666H21.6667V21.6666Z"
                />
              </svg>
            </div>

            <div class="px-4 py-2 -mx-3">
              <div class="mx-3">
                <span class="font-semibold text-yellow-500">Warning</span>
                <p class="text-sm text-gray-600">Image size is too large.</p>
              </div>
            </div>
          </div>

          <div
              class="inline-flex w-full max-w-sm ml-3 overflow-hidden bg-white rounded-lg shadow-md"
          >
            <div class="flex items-center justify-center w-12 bg-red-500">
              <svg
                  class="w-6 h-6 text-white fill-current"
                  viewBox="0 0 40 40"
                  xmlns="http://www.w3.org/2000/svg"
              >
                <path
                    d="M20 3.36667C10.8167 3.36667 3.3667 10.8167 3.3667 20C3.3667 29.1833 10.8167 36.6333 20 36.6333C29.1834 36.6333 36.6334 29.1833 36.6334 20C36.6334 10.8167 29.1834 3.36667 20 3.36667ZM19.1334 33.3333V22.9H13.3334L21.6667 6.66667V17.1H27.25L19.1334 33.3333Z"
                />
              </svg>
            </div>

            <div class="px-4 py-2 -mx-3">
              <div class="mx-3">
                <span class="font-semibold text-red-500">Error</span>
                <p class="text-sm text-gray-600">Your email is already used!</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--     Drop down-->
    <div class="mt-8">
      <h4 class="text-gray-600">Drop down</h4>

      <div class="mt-4">
        <!--        <div class="flex items-center px-4 py-4 space-x-4 overflow-x-auto bg-white rounded-md">-->

        <div>
          <Multiselect
              v-model="value"
              mode="tags"
              placeholder="Select your characters"
              :options="options"
              :searchable="true"
              :hideSelectedTag="false"
          />

        </div>
        <!--        </div>-->
      </div>
    </div>

    <!-- Inputs -->
    <div class="mt-8">
      <h4 class="text-gray-600">Inputs</h4>

      <div class="mt-4">
        <div class="flex items-center px-4 py-4 space-x-4 overflow-x-auto bg-white rounded-md">
          <label>
            <input
                type="radio"
                class="w-5 h-5 text-indigo-600 focus:ring-indigo-500"
                name="radio"
                v-model="radioValue"
            /><span class="ml-2 text-gray-700">Radio</span>
          </label>

          <label>
            <input
                type="checkbox"
                class="w-5 h-5 text-indigo-600 rounded-md focus:ring-indigo-500"
                name="checkbox"
                v-model="checkboxValue"
            /><span class="ml-2 text-gray-700">Checkbox</span>
          </label>

          <label class="block">
            <input
                type="email"
                placeholder="Email"
                class="block w-full mt-1 border-gray-200 rounded-md focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500"
            />
          </label>

          <!--          <div class="relative mx-4 lg:mx-0">
                      <span class="absolute inset-y-0 left-0 flex items-center pl-3">
                        <svg class="w-5 h-5 text-gray-500" viewBox="0 0 24 24" fill="none">
                          <path
                            d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z"
                            stroke="currentColor"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                        </svg>
                      </span>

                      <input
                        class="w-32 pl-10 pr-4 text-indigo-600 border-gray-200 rounded-md sm:w-64 focus:border-indigo-600 focus:ring focus:ring-opacity-40 focus:ring-indigo-500"
                        type="text"
                        placeholder="Search"
                      />
                    </div>-->
        </div>
      </div>
    </div>

    <!-- Buttons -->
    <div class="mt-8">
      <h4 class="text-gray-600">Buttons</h4>

      <div class="mt-4">
        <div class="flex px-4 py-4 space-x-4 overflow-x-auto bg-white rounded-md">
          <button
              class="px-4 py-2 font-medium tracking-wide text-white capitalize transition-colors duration-200 transform bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
            Primary
          </button>

          <button
              class="flex items-center px-2 py-2 font-medium tracking-wide text-white capitalize transition-colors duration-200 transform bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
            <svg class="w-5 h-5 mx-1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd"
                    d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 010 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
                    clip-rule="evenodd"/>
            </svg>
            <span class="mx-1">Refresh</span>
          </button>


          <div class="flex items-center">
            <button
                class="px-4 py-2 font-medium tracking-wide text-white capitalize transition-colors duration-200 transform bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
              Download
            </button>

            <span class="border border-transparent"></span>

            <div class="relative">
              <!-- Menu toggle button -->
              <button
                  class="relative z-10 block p-2 transition-colors duration-200 transform bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                <svg class="w-6 h-6 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                     stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/>
                </svg>
              </button>

              <!-- Menu list -->
              <div
                  class="absolute right-0 z-20 w-48 mt-2 overflow-hidden bg-white rounded-md shadow-xl dark:bg-gray-700"></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Paginations -->
    <div class="mt-8">
      <h4 class="text-gray-600">Paginations</h4>

      <div class="mt-4">
        <div class="flex px-4 py-4 overflow-x-auto bg-white rounded-md">
          <div class="flex mr-4 rounded">
            <a
                href="#"
                class="px-3 py-2 ml-0 leading-tight text-indigo-700 bg-white border border-r-0 border-gray-200 rounded-l hover:bg-indigo-500 hover:text-white"
            ><span>Previous</span></a
            >
            <a
                href="#"
                class="px-3 py-2 leading-tight text-indigo-700 bg-white border border-r-0 border-gray-200 hover:bg-indigo-500 hover:text-white"
            ><span>1</span></a
            >
            <a
                href="#"
                class="px-3 py-2 leading-tight text-indigo-700 bg-white border border-r-0 border-gray-200 hover:bg-indigo-500 hover:text-white"
            ><span>2</span></a
            >
            <a
                href="#"
                class="px-3 py-2 leading-tight text-indigo-700 bg-white border border-r-0 border-gray-200 hover:bg-indigo-500 hover:text-white"
            ><span>3</span></a
            >
            <a
                href="#"
                class="px-3 py-2 leading-tight text-indigo-700 bg-white border border-gray-200 rounded-r hover:bg-indigo-500 hover:text-white"
            ><span>Next</span></a
            >
          </div>
        </div>
      </div>
    </div>
    <!-- Double Click Test -->
    <div class="mt-8">
      <h4 class="text-gray-600">Double Click</h4>

      <div class="mt-4 bg-white p-4 rounded-md">

        <button
            class="px-4 py-2 bg-indigo-600 text-white rounded-md"
            @dblclick="handleDoubleClick"
        >
          Double Click Me
        </button>

        <p
            v-if="doubleClicked"
            class="mt-4 text-green-600"
        >
          Double click detected!
        </p>

      </div>
    </div>

    <!-- DropDown Test Zone -->
    <div class="mt-8">
      <h4 class="text-gray-600">DropDown Test</h4>

      <div class="mt-4 bg-white p-4 rounded-md space-y-4">

        <!-- SINGLE SELECT -->
        <select
            class="border p-2 rounded-md"
            v-model="singleValue"
        >
          <option value="">Select value</option>
          <option value="1">One</option>
          <option value="2">Two</option>
          <option value="3">Three</option>
        </select>

        <p class="text-gray-700">
          Selected value: {{ singleValue }}
        </p>

        <!-- MULTI SELECT -->
        <select
            class="border p-2 rounded-md w-64"
            multiple
            size="1"
            v-model="multiValue"
        >
          <option value="A">Alpha</option>
          <option value="B">Beta</option>
          <option value="C">Gamma</option>
        </select>

        <p class="mt-2 text-gray-700">
          Selected values: {{ multiValue.join(',') }}
        </p>

      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">HotKey Test</h4>

      <div class="mt-4 bg-white p-4 rounded-md space-y-4">

        <input
            type="text"
            class="border p-2 w-64"
            v-model="input1"
            placeholder="Input 1"
        />

        <input
            type="text"
            class="border p-2 w-64"
            v-model="input2"
            placeholder="Input 2"
            @keydown.enter="handleEnter"
        />

        <p class="text-gray-700">
          Result: {{ result }}
        </p>

        <div>
          <button
              class="px-4 py-2 bg-indigo-600 text-white rounded-md"
              @click="openModal"
          >
            Open Modal
          </button>

          <div
              v-if="modalOpen"
              ref="modal"
              tabindex="0"
              @keydown.esc="closeModal"
              class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
          >
            <div class="bg-white p-4 rounded-md">
              <p class="text-gray-700">Modal Open</p>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Hover Test</h4>

      <div class="mt-4 bg-white p-4 rounded-md">

        <div
            class="p-4 bg-indigo-500 text-white w-48 text-center"
            @mouseenter="hovered = true"
            @mouseleave="hovered = false"
        >
          Hover over me
        </div>

        <p class="mt-2 text-gray-700">
          Hover status: {{ hovered }}
        </p>

      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Image Test</h4>

      <div class="mt-4 bg-white p-4 rounded-md">

        <img
            src="/images/test-image.png"
            alt="Test Image"
            data-test="test-image"
            class="w-64 border"
        />

      </div>
    </div>

    <button
        data-test="set-cookie"
        @click="setCookie"
        class="px-4 py-2 bg-indigo-600 text-white rounded-md"
    >
      Set Cookie
    </button>

    <!-- WAIT TEST BLOCK -->
    <div class="mt-8">
      <h4 class="text-gray-600">Wait Test</h4>

      <div class="mt-4 bg-white p-4 rounded-md space-y-4">

        <!-- INPUTS -->
        <input
            data-test="wait-time"
            type="number"
            v-model="waitTime"
            class="border p-2 w-32"
            placeholder="Time"
        />

        <select
            data-test="wait-unit"
            v-model="waitUnit"
            class="border p-2"
        >
          <option value="seconds">Seconds</option>
          <option value="millis">Millis</option>
        </select>

        <!-- BUTTON -->
        <button
            data-test="wait-start"
            @click="startWait"
            class="px-4 py-2 bg-indigo-600 text-white rounded-md"
        >
          Start Wait
        </button>

        <!-- RESULT -->
        <p v-if="done"
           data-test="wait-result"
           class="text-green-600">
          Done
        </p>

        <!-- DELAYED ELEMENT -->
        <button
            v-if="showDelayed"
            data-test="wait-clickable"
            class="px-4 py-2 bg-green-600 text-white rounded-md"
        >
          Clickable Button
        </button>

      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Test Iframe</h4>

      <div class="mt-4 bg-white p-4 rounded-md">

        <iframe
            data-test="test-frame"
            src="/iframe-test.html"
            class="w-full h-64 border"
        ></iframe>

      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Video in other frame</h4>
      <div class="w-full h-full">
        <div class="embed-responsive embed-responsive-16by9 py-8">
          <iframe class="embed-responsive-item" src="https://www.youtube.com/embed/6ihAN0WE1vo"
                  allowfullscreen></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Multiselect from '@vueform/multiselect'

export default {
  components: {Multiselect},
  data() {
    return {
      testImageUrl: new URL('/images/test-image.png', window.location.origin).href,
      value: [],
      input1: '',
      input2: '',
      result: '',
      modalOpen: false,
      hovered: false,
      singleValue: '',
      multiValue: [] as string[],
      options: ['Batman', 'Robin', 'Joker'],
      radioValue: false,
      checkboxValue: false,
      doubleClicked: false,
      waitTime: 1,
      waitUnit: 'seconds',
      done: false,
      showDelayed: false,
    }
  },
  methods: {
    handleDoubleClick() {
      this.doubleClicked = !this.doubleClicked
    },
    handleEnter() {
      this.result = this.input2
    },

    openModal() {
      this.modalOpen = true
    },

    setCookie() {
      document.cookie = "testCookie=cookieValue"
    },

    closeModal() {
      this.modalOpen = false
    },

    startWait() {
      this.done = false
      this.showDelayed = false
      const time =
          this.waitUnit === 'seconds'
              ? this.waitTime * 1000
              : this.waitTime

      setTimeout(() => {
        this.done = true
        this.showDelayed = true
      }, time)
    }
  },
  watch: {
    modalOpen(val) {
      if (val) {
        this.$nextTick(() => {
          (this.$refs.modal as HTMLElement)?.focus()
        })
      }
    }
  }
}
</script>
<style src="@vueform/multiselect/themes/default.css"></style>