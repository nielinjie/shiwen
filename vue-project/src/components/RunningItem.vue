<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useRunPromptStore } from "../stores/runPrompt";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Select, SelectOption ,Row,Col} from "ant-design-vue";
import { computed, ref, watch } from "vue";
import { useClientStore } from "@/stores/clientStore";
const rpStore = useRunPromptStore();
const workspaceStore = useWorkspaceStore();
const props = defineProps<{ index: number; x: number; y: number }>();
const { cells } = storeToRefs(workspaceStore);
const { clients } = storeToRefs(useClientStore());
const tasks = computed(() => {
    return (
        cells.value.find((cell) => {
            return cell.x === props.x && cell.y === props.y;
        })?.tasks ?? []
    );
});
function removeMe() {
    tasks.value.splice(props.index, 1);
}
</script>

<template>
    <Row>
        <Select v-model:value="tasks[props.index].client">
            <SelectOption
                v-for="client in clients"
                :key="client"
                :value="client"
                >{{ client }}</SelectOption
            >
        </Select>
        <Button @click="rpStore.runIt(x,y,index, tasks[props.index].client)">
            <span>Run</span>
        </Button>
        <Button @click="removeMe">-</Button>
    </Row>
    <Row>
        <div class="variable">
            <textarea v-model="tasks[index].result"></textarea>
        </div>
    </Row>
</template>

<style scoped>
.variable {
    padding: 1em;
    background-color: bisque;
    /* height: 100%; */
}
.variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 1em;
    border-color: darkcyan;
}
</style>
