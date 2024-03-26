<script setup lang="ts">
import { useWorkspaceStore, type WorkCell } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { Button, Space, Row, Col, TypographyTitle, Flex } from "ant-design-vue";
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
    <Flex :vertical="true" gap="middle">
        <Flex gap="large" align="end">
            <TypographyTitle :level="5">
                {{ x === 0 && y === 0 ? "Calling:" : "&nbsp;" }}
            </TypographyTitle>
            <Button @click="addTask"> + </Button>
        </Flex>
        <Flex gap="small">
            <div v-for="(task, index) in tasks">
                <RunningItem :index="index" :x="x" :y="y"></RunningItem>
            </div>
        </Flex>
    </Flex>
</template>
