<script setup lang="ts">
import { useWorkspaceStore, type WorkCell } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { Button, Space, Row, Col, TypographyTitle } from "ant-design-vue";
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
    <Row>
        <Col>
            <TypographyTitle :level="4">
                Calling:
            </TypographyTitle>
        </Col>
        <Col :offset="12" :span="12">
            <Button @click="addTask">
                Add
            </Button>
        </Col>
    </Row>
    <Row>
        <Col v-for="(task, index) in tasks">
            <RunningItem :index="index" :x="x" :y="y"></RunningItem>
        </Col>
    </Row>
</template>
