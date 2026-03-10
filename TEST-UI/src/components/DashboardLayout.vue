<template>
  <div :class="['flex', 'bg-gray-200', 'font-roboto', currentRoute.path === '/cards' ? 'h-[120vh]' : 'h-screen']">
    <Sidebar />

    <div class="flex-1 flex flex-col overflow-hidden">
      <Header />

      <main ref="scrollContainer" class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200">
        <div class="container mx-auto px-6 py-8">
          <slot />
        </div>
        <button @click="backToTop" :disabled="!isEnabled" :class="{ invisible: !isEnabled }" class="to-top">
          Up
        </button>
      </main>
    </div>
  </div>
  <template v-if="currentRoute.path === '/cards'">
    <Footer />
  </template>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Sidebar from './Sidebar.vue';
import Header from './Header.vue';
import Footer from './Footer.vue';
import { useRouter } from "vue-router";

const { currentRoute } = useRouter();

const isEnabled = ref(false);
const scrollContainer = ref<HTMLElement | null>(null);

const backToTop = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollTop = 0;
    console.log(scrollContainer.value);
  } else {
    console.log('nope');
  }
};

onMounted(() => {
  const container = scrollContainer.value;
  if (container) {
    container.addEventListener('scroll', () => {
      if (container.scrollTop + container.clientHeight >= container.scrollHeight) {
        isEnabled.value = true;
        console.log('chto ugodno');
      } else {
        isEnabled.value = false;
      }
    });
  }
});
</script>

<style>
.btn {
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.55);
  padding-top: 27px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 5px;
}
</style>