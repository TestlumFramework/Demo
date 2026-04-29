<template>
  <div class="p-6">

    <h3 data-test="scroll-title" class="text-2xl mb-4">
      Scroll Test Page
    </h3>

    <div
        data-test="scroll-inner"
        class="h-[300px] overflow-y-auto border p-4 mb-10"
    >
      <div class="h-[1200px]">

        <div data-test="inner-top" class="mb-10">
          TOP INNER
        </div>

        <div data-test="inner-middle" class="mt-[500px]">
          MIDDLE INNER
        </div>

        <div data-test="inner-bottom" class="mt-[500px]">
          BOTTOM INNER
        </div>

      </div>
    </div>

    <div class="h-[1500px]">

      <div data-test="page-top">
        TOP PAGE
      </div>

      <div data-test="page-middle" class="mt-[700px]">
        MIDDLE PAGE
      </div>

      <div data-test="page-bottom" class="mt-[700px]">
        BOTTOM PAGE
      </div>

    </div>

  </div>
</template>

<script lang="ts">
export default {
  name: 'ScrollTest'
}
</script>