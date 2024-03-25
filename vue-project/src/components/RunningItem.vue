<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useRunPromptStore } from "../stores/runPrompt";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Select, SelectOption } from "ant-design-vue";
import { computed, ref, watch } from "vue";
import { useClientStore } from "@/stores/clientStore";
const rpStore = useRunPromptStore();
const workspaceStore = useWorkspaceStore();
const props = defineProps<{index:number,x:number,y:number}>();
const { cells } = storeToRefs(workspaceStore);
const { clients } = storeToRefs(useClientStore());
const tasks = computed(() => {
    return (cells.value
        .find((cell) => {
            return cell.x === props.x && cell.y === props.y;
        })
        ?.tasks ) ?? [];
});
function removeMe() {
    tasks.value.splice(props.index, 1);
}


</script>

<template>
    <div class="variable">
        <Select v-model:value="tasks[props.index].client">
            <SelectOption
                v-for="client in clients"
                :key="client"
                :value="client"
                >{{ client }}</SelectOption
            >
        </Select>
        <Button @click="rpStore.runIt(index, tasks[props.index].client)">
            <span>Run</span>
        </Button>
        <Button @click="removeMe">-</Button>
        <textarea v-model="tasks[index].result"></textarea>
    </div>
</template>

<style scoped>
.variable {
    padding: 10px;
    background-color: bisque;
    height: 100%;
}
.variable textarea {
    font-family: Arial, sans-serif; /* Replace with your desired font */
    font-size: 16px; /* Replace with your desired font size */
}
textarea {
    width: 100%;
    height: 80%;
}
</style>
