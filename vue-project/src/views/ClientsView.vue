<script setup lang="ts">
import { useClientStore } from "@/stores/clientStore";
import {
    Button,
    Flex,
    TypographyParagraph,
    TypographyTitle,
} from "ant-design-vue";
import JsonEditorVue from "json-editor-vue";
import { SaveOutlined } from "@ant-design/icons-vue";
import { h } from "vue";
const clientStore = useClientStore();
function saveConfigs() {
    clientStore.saveConfigs();
}
</script>
<template>
    <Flex gap="large">
        <JsonEditorVue
            :content="{ text: clientStore.configs }"
            :onChange="(t) => (clientStore.configs = t.text)"
        >
        </JsonEditorVue>
        <Flex vertical gap="large">
            <div>
                <Button
                    size="large"
                    @click="saveConfigs"
                    type="primary"
                    :icon="h(SaveOutlined)"
                >
                    Save
                </Button>
            </div>
            <TypographyTitle :level="5">LLM的Api和Client</TypographyTitle>
            <TypographyParagraph
                >https://ollama.com/library</TypographyParagraph
            >
        </Flex>
    </Flex>
</template>
