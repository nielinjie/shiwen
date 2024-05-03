<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useRunPromptStore } from "./runPrompt";
import { useWorkspaceStore, type WorkCell } from "@/stores/workspaceStore";
import { Button, Select, SelectOption, Flex, Textarea } from "ant-design-vue";
import { computed, ref } from "vue";
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
const hover = ref(false);
</script>

<template>
    <div @mousemove="hover = true" @mouseout="hover = false">
        <Flex :vertical="true" gap="small" class="content">
            <Flex gap="large">
                <Flex gap="2">
                    <Select
                        size="small"
                        v-model:value="tasks[props.index].client"
                    >
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
                            rpStore.runIt(
                                x,
                                y,
                                index,
                                tasks[props.index].client
                            )
                        "
                    >
                        <CaretRightOutlined />
                    </Button>
                </Flex>
                <div v-visibility="hover">
                    <Flex gap="2">
                        <Button size="small" shape="circle" @click="removeMe">
                            <MinusOutlined />
                        </Button>
                        <Button
                            size="small"
                            shape="circle"
                            v-if="index === tasks.length - 1"
                            @click="addTask"
                        >
                            <PlusOutlined />
                        </Button>
                    </Flex>
                </div>
            </Flex>
            <div class="variable">
                <Textarea
                    v-model:value="tasks[index].result"
                    placeholder="Result here..."
                    :autoSize="{ minRows: 6, maxRows: 12 }"
                ></Textarea>
            </div>
        </Flex>
    </div>
</template>

<style scoped>
.content {
    min-width: 5vw;
}
</style>
@/matrix/runPrompt@/matrix/workspaceStore../stores/workspaceStore
