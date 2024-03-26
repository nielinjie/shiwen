<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { TypographyText, Row, Flex, Button } from "ant-design-vue";
import { PlusOutlined, MinusOutlined } from "@ant-design/icons-vue";

const { prompts } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ promptIndex: number }>();
const workspaceStore = useWorkspaceStore();
</script>
<template>
    <Flex :vertical="true" gap="middle">
        <Flex align="flex-end" gap="large" justify="flex-end">
            <TypographyText strong>
                {{ props.promptIndex === 0 ? "Prompts:" : "&nbsp;" }}
            </TypographyText>
            <Flex :gap="2">
                <Button
                    size="small"
                    shape="circle"
                    @click="workspaceStore.removePrompt(promptIndex)"
                >
                    <MinusOutlined
                /></Button>
                <Button
                    size="small"
                    shape="circle"
                    v-if="props.promptIndex === 0"
                    @click="workspaceStore.addPrompt"
                >
                    <PlusOutlined
                /></Button>
            </Flex>
        </Flex>

        <div class="variable">
            <textarea v-model="prompts[props.promptIndex]"></textarea>
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
