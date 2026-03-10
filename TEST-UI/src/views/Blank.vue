<template>
    <div class="container mx-auto py-4">
        <h3 class="text-gray-700 text-3xl font-medium">Drag&Drop Board</h3>
    </div>
    <div class="flex flex-wrap -mx-6 py-3">
        <div class="w-full px-6 sm:w-1/2 xl:w-1/2">
            <h5 class="text-2xl font-medium text-gray-700 py-2">Drop zone</h5>
            <div class="flex items-center px-5 py-6 bg-white rounded-md shadow-sm" @drop="onDrop($event, 1)"
                 @dragenter.prevent
                 @dragover.prevent>
                <div v-for="item in getList(1)" :key="item.id"
                     class="items-center px-2 py-3 mx-3 my-3 rounded-md shadow-sm bg-gray-600 text-gray-100"
                     draggable="true"
                     @dragstart="startDrag($event, item)">
                    {{ item.title }}
                </div>
            </div>
        </div>
        <div class="w-full px-6 sm:w-1/2 xl:w-1/2">
            <h5 class="text-2xl font-medium text-gray-700 py-2">Also drop zone</h5>
            <div class="flex items-center px-5 py-6 bg-white rounded-md shadow-sm" @drop="onDrop($event, 2)"
                 @dragenter.prevent
                 @dragover.prevent>
                <div v-for="item in getList(2)" :key="item.id"
                     class="items-center px-2 py-3 mx-3 my-3 rounded-md shadow-sm bg-gray-600 text-gray-100"
                     draggable="true"
                     @dragstart="startDrag($event, item)">
                    {{ item.title }}
                </div>
            </div>
        </div>
    </div>

    <div @drop.prevent="drop" @change="selectedFile" :class="{ 'disabled-dropzone': draggingItemActive }">
        <div @dragenter.prevent="toggleActive" @dragleave.prevent="toggleActive" @dragover.prevent
             @drop.prevent="toggleActive" :class="{ 'active-dropzone': active }" class="dropzone">
            <span class="dropzone-text">Drag and Drop File here</span>
            <span class="file-info" id="fileDivName">File: {{ dropzoneFile.name }}</span>
        </div>
        <div class="file-input-container">
            <input type="file" id="dropzoneFile" class="dropzoneFile" @change="selectedFile"/>
            <span class="file-info" id="fileInputName">File: {{ fileName }}</span>
        </div>
    </div>
</template>

<script lang="ts">
import {ref, onMounted, onUnmounted} from 'vue'

export default {
    name: "DropZone",
    setup() {

        interface Item {
            id: number;
            title: string;
            list: number;
        }


        const items = ref([
            {id: 0, title: 'drag me', list: 1},
            {id: 1, title: 'drag me', list: 1},
            {id: 2, title: 'drag me', list: 1},
            {id: 3, title: 'drag me', list: 2}
        ])

        const draggingItemActive = ref(false);

        const getList = (list: number) => {
            return items.value.filter((item) => item.list == list)
        }

        const startDrag = (event: any, item: Item) => {
            active.value = false
            draggingItemActive.value = true
            event.dataTransfer.dropEffect = 'move'
            event.dataTransfer.effectAllowed = 'move'
            event.dataTransfer.setData('itemID', item.id)
        }

        const onDrop = (event: any, list: number) => {
            event.stopPropagation()
            draggingItemActive.value = true;
            const itemID = event.dataTransfer.getData('itemID')
            const item = items.value.find((item) => item.id == itemID)

            if (item) {
                item.list = list
                draggingItemActive.value = false;
            }
        }

        const active = ref(false);
        const toggleActive = () => {
            active.value = !active.value;
        };

        let dropzoneFile = ref<any>("");
        const drop = (e: any) => {
            dropzoneFile.value = e.dataTransfer.files[0];
        };

        let fileName = ref(""); // Define fileName variable to hold the selected file name

        const selectedFile = (e: any) => {
            const file = e.target.files[0];
            if (file) {
                fileName.value = file.name; // Update fileName with the selected file's name
            }
        };

        const handleDragOver = (event: DragEvent) => {
            event.preventDefault()
        }

        const handleDrop = (event: DragEvent) => {
            event.preventDefault()

            const itemID = event.dataTransfer?.getData('itemID')
            if (itemID) {
                const item = items.value.find((item) => item.id === parseInt(itemID))
                if (item) {
                    // Handle drop outside left and right dropzone block
                    draggingItemActive.value = false;
                }
            }
        }
        onMounted(() => {
            document.addEventListener('dragover', handleDragOver)
            document.addEventListener('drop', handleDrop)
        })

        onUnmounted(() => {
            document.removeEventListener('dragover', handleDragOver)
            document.removeEventListener('drop', handleDrop)
        })

        return {
            dropzoneFile, drop, selectedFile,
            fileName,
            getList,
            onDrop,
            startDrag,
            active,
            draggingItemActive,
            toggleActive
        }
    },
}
</script>

<style scoped lang="scss">
.dropzone-text {
  font-size: 18px;
  color: #555;
  margin-bottom: 10px;
}

.dropzone:hover {
  border-color: #41b883;
}

.file-info {
  font-size: 18px;
  color: #777;
  margin-top: 10px;
}

.file-input-container {
    margin-top: 20px;
}

.dropzone {
  width: 400px;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  row-gap: 16px;
  border: 2px dashed #41b883;
  background-color: #fff;
  transition: 0.3s ease all;

  label {
    padding: 8px 12px;
    color: #fff;
    background-color: #41b883;
    transition: 0.3s ease all;
  }

  input {
    display: none;
  }
}

.active-dropzone {
  color: #fff;
  border-color: #fff;
  background-color: #41b883;

  label {
    background-color: #fff;
    color: #41b883;
  }
}

.disabled-dropzone {
  pointer-events: none;
  opacity: 0.6;
  z-index: 20;
}
</style>
