<script setup lang="ts">
import { useWorkspaceStore, type WorkCell } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { Button, Space, Row, Col } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { useClientStore } from "@/stores/clientStore";
import { computed } from "vue";

const rpStore = useRunPromptStore();
const { cells } = storeToRefs(useWorkspaceStore());
const { clients } = storeToRefs(useClientStore());
const props = defineProps<{ x: number; y: number }>();
function addTask() {
    cells.value
        .find((cell: WorkCell) => {
            return cell.x === props.x && cell.y === props.y;
        })
        ?.tasks?.push({
            result: "",
            client: clients.value[0],
        });
}
const tasks = computed(() => {
    return (
        cells.value.find((cell: WorkCell) => {
            return cell.x === props.x && cell.y === props.y;
        })?.tasks ?? []
    );
});
</script>
<template>
    <Row class="main">
        <Col v-for="(task, index) in tasks">
            <RunningItem :index="index" :x="x" :y="y"></RunningItem>
        </Col>
        <Col>
            <Button @click="addTask">
                <span>Add</span>
            </Button>
        </Col>
    </Row>
</template>
<style scoped>
.main {
    height: 400px;
}
</style>
