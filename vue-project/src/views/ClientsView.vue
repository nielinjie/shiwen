<script setup lang="ts">
import { useClientStore } from "@/stores/clientStore";
import {
    Button,
    Flex,
    TypographyParagraph,
    TypographyText,
    TypographyTitle,
} from "ant-design-vue";
import JsonEditorVue from "json-editor-vue";
import { SaveOutlined, UploadOutlined } from "@ant-design/icons-vue";
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
            <Flex gap="large">
                <Button
                    size="large"
                    @click="saveConfigs"
                    type="primary"
                    :icon="h(SaveOutlined)"
                >
                    Save
                </Button>
                <Button
                    size="large"
                    @click="clientStore.loadConfigs"
                    :icon="h(UploadOutlined)"
                    >Load</Button
                >
            </Flex>
            <Flex vertical>
                <TypographyTitle :level="5">Api和Client配置:</TypographyTitle>
                <TypographyParagraph>载入示例配置:
                <Button  size="small"
                    @click="clientStore.loadExample"
                    :icon="h(UploadOutlined)"></Button></TypographyParagraph>
                <TypographyTitle :level="5">LLM的Api和Client</TypographyTitle>
                <TypographyParagraph
                    >https://ollama.com/library</TypographyParagraph
                >
            </Flex>
        </Flex>
    </Flex>
</template>
