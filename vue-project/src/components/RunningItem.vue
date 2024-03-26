<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useRunPromptStore } from "../stores/runPrompt";
import { useWorkspaceStore, type WorkCell } from "@/stores/workspaceStore";
import { Button, Select, SelectOption, Flex } from "ant-design-vue";
import { computed } from "vue";
import { useClientStore } from "@/stores/clientStore";
import {
    PlusOutlined,
    MinusOutlined,
    CaretRightOutlined,
} from "@ant-design/icons-vue";

const rpStore = useRunPromptStore();
const workspaceStore = useWorkspaceStore();
const props = defineProps<{ index: number; x: number; y: number }>();
const { cells } = storeToRefs(workspaceStore);
const { clients } = storeToRefs(useClientStore());
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
    <Flex :vertical="true" gap="small">
        <Flex gap="large">
            <Flex gap="2">
                <Select size="small" v-model:value="tasks[props.index].client">
                    <SelectOption
                        v-for="client in clients"
                        :key="client"
                        :value="client"
                        >{{ client }}</SelectOption
                    >
                </Select>
                <Button
                    size="small"
                    shape="circle"
                    @click="
                        rpStore.runIt(x, y, index, tasks[props.index].client)
                    "
                >
                    <CaretRightOutlined />
                </Button>
            </Flex>
            <Flex gap="2">
                <Button size="small" shape="circle" @click="removeMe">
                    <MinusOutlined />
                </Button>
                <Button
                    size="small"
                    shape="circle"
                    v-if="index === 0"
                    @click="addTask"
                >
                    <PlusOutlined />
                </Button>
            </Flex>
        </Flex>
        <div class="variable">
            <textarea v-model="tasks[index].result"></textarea>
        </div>
    </Flex>
</template>

<style scoped>
.variable {
    padding: 2px;
    /* height: 100%; */
}
.variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 0.5em;
    border-color: rgba(179, 223, 247, 0.25);
    height: 15vh;
}
</style>
