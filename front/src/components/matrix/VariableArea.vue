<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "@/stores/runPrompt";
import { storeToRefs } from "pinia";
import { TypographyText, Row, Flex, Button, Textarea } from "ant-design-vue";
import { PlusOutlined, MinusOutlined } from "@ant-design/icons-vue";
import { ref } from "vue";

const { variables } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ variableIndex: number }>();
const workspaceStore = useWorkspaceStore();
const hover = ref(false);
</script>

<template>
    <div @mouseover="hover = true" @mouseout="hover = false">
        <Flex :vertical="true" gap="middle">
            <Flex align="flex-start" gap="large" justify="flex-end">
                <TypographyText strong>
                    {{ props.variableIndex === 0 ? "Variables:" : "&nbsp;" }}
                </TypographyText>
                <div v-visibility="hover">
                    <Flex :gap="2">
                        <Button
                            size="small"
                            shape="circle"
                            @click="
                                workspaceStore.removeVariable(variableIndex)
                            "
                        >
                            <MinusOutlined
                        /></Button>
                        <Button
                            size="small"
                            shape="circle"
                            v-if="props.variableIndex === variables.length - 1"
                            @click="workspaceStore.addVariable()"
                        >
                            <PlusOutlined
                        /></Button>
                    </Flex>
                </div>
            </Flex>
            <div class="variable">
                <Textarea
                    v-model:value="variables[props.variableIndex]"
                    allowClear
                    :autoSize="{
                        minRows: 3,
                        maxRows: 10,
                    }"
                    placeholder="Enter Variables here..."
                ></Textarea>
            </div>
        </Flex>
    </div>
</template>

<style scoped>
.variable {
    padding: 2px;
}
/* .variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 0.5em;
    border-color: rgba(179, 223, 247, 0.25);
    height: 15vh;
} */
</style>
