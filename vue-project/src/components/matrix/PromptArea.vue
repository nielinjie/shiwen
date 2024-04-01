<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { TypographyText, Row, Flex, Button, Textarea } from "ant-design-vue";
import { PlusOutlined, MinusOutlined, SaveOutlined } from "@ant-design/icons-vue";
import { ref } from "vue";
import { usePromptLibStore } from "@/stores/promptLibStore";

const { prompts } = storeToRefs(useWorkspaceStore());
const promptLib = usePromptLibStore();
const props = defineProps<{ promptIndex: number }>();
const workspaceStore = useWorkspaceStore();
const hover = ref(false);
function head10(content:string):string{
    return content.length>10?content.slice(0,10):content;
}
</script>
<template>
    <div @mouseover="hover = true" @mouseout="hover = false">
        <Flex vertical gap="middle" class="content">
            <Flex align="flex-end" gap="large" justify="flex-end">
                <TypographyText strong>
                    {{ props.promptIndex === 0 ? "Prompts:" : "&nbsp;" }}
                </TypographyText>
                <div v-visibility="hover">
                    <Flex :gap="2">
                        <Button
                            size="small"
                            shape="circle"
                            @click="promptLib.savePrompt(prompts[props.promptIndex],head10(prompts[props.promptIndex]),'local-storage')"
                        ><SaveOutlined/></Button>
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
                            v-if="props.promptIndex === prompts.length - 1"
                            @click="workspaceStore.addPrompt()"
                        >
                            <PlusOutlined
                        /></Button>
                    </Flex>
                </div>
            </Flex>

            <div class="variable">
                <Textarea
                    v-model:value="prompts[props.promptIndex]"
                    allowClear
                    :autoSize="{ minRows: 3, maxRows: 10 }"
                    placeholder="Enter prompt here..."
                ></Textarea>
            </div>
        </Flex>
    </div>
</template>

<style scoped>
.content {
    min-width: 10vw;
}
/* padding: 2px; */
/* height: 100%; */

/* .variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 0.5em;
    border-color: rgba(179, 223, 247, 0.25);
    height: 15vh;
} */
</style>
