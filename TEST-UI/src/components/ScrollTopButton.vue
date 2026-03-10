<template>
  <div ref="scrollTopButton" class="sticky sticky w-full flex justify-end bottom-0 pb-3 pr-5 lg:pr-16 transition">
    <div class="text-gray-400 hover:text-blue-400 transition">
      <button @click="scrollToTop">
        Scroll to top
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },

  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
  },

  methods: {
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
    handleScroll() {
      const scrollBtn: any = this.$refs.scrollTopButton;

      if (window.scrollY > 0) {
        scrollBtn.classList.remove("invisible");
      } else {
        scrollBtn.classList.add("invisible");
      }
    },
  },
});
</script>

<style>
.to-top {
  background: white;
  position: sticky;
  bottom: 16px;
  right: 32px;
  width: 50px;
  height: 50px;
  border-radius: 10%;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #1f1f1f;
  text-decoration: none;
  cursor: pointer;
  transition: 0.5s;
  z-index: 1;
}

.invisible {
  opacity: 0;
}
</style>

