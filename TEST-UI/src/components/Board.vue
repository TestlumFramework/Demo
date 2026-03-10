<template>
  <div class="flex flex-wrap -mx-6">
    <div class="w-full px-6 sm:w-1/2 xl:w-1/2">
      <h5 class="text-2xl font-medium text-gray-700">Drop zone</h5>
      <div class="flex items-center px-5 py-6 bg-white rounded-md shadow-sm" @drop="onDrop($event, 1)" @dragenter.prevent
        @dragover.prevent>
        <div v-for="item in getList(1)" :key="item.id" class="items-center px-2 py-3 mx-3 my-3 rounded-md shadow-sm"
          draggable="true" @dragstart="startDrag($event, item)">
          {{ item.title }}
        </div>
      </div>
    </div>
    <div class="w-full px-6 sm:w-1/2 xl:w-1/2">
      <h5 class="text-2xl font-medium text-gray-700">Also drop zone</h5>
      <div class="flex items-center px-5 py-6 bg-white rounded-md shadow-sm" @drop="onDrop($event, 2)" @dragenter.prevent
        @dragover.prevent>
        <div v-for="item in getList(2)" :key="item.id" class="items-center px-2 py-3 mx-3 my-3 rounded-md shadow-sm"
          draggable="true" @dragstart="startDrag($event, item)">
          {{ item.title }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'

export default {
  setup() {

    interface Item {
      id: number;
      title: string;
      list: number;
    }

    const items = ref<Item[]>([
      { id: 0, title: 'drag me', list: 1 },
      { id: 1, title: 'drag me', list: 1 },
      { id: 2, title: 'drag me', list: 1 },
      { id: 3, title: 'drag me', list: 2 }
    ])

    const getList = (list: number) => {
      return items.value.filter((item) => item.list == list)
    }

    const startDrag = (event: any, item: Item) => {
      console.log(item)
      event.dataTransfer.dropEffect = 'move'
      event.dataTransfer.effectAllowed = 'move'
      event.dataTransfer.setData('itemID', item.id)
    }

    const onDrop = (event: any, list: number) => {
      const itemID = event.dataTransfer.getData('itemID')
      const item = items.value.find((item) => item.id == itemID)
      if (item) {
        item.list = list
      }
    }

    return {
      getList,
      onDrop,
      startDrag
    }
  },
}
</script>
