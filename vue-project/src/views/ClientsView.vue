<script setup lang="ts">
import { useClientStore } from "@/stores/clientStore";
import {
    Button,
    Flex,
    TypographyParagraph,
    TypographyText,
    TypographyTitle,
} from "ant-design-vue";
import { SaveOutlined, UploadOutlined } from "@ant-design/icons-vue";
import { h } from "vue";
import { VAceEditor } from "vue3-ace-editor";
import "ace-builds/src-noconflict/mode-json"; // Load the language definition file used below
import "ace-builds/src-noconflict/theme-chrome"; // Load the theme definition file used below
const clientStore = useClientStore();
function saveConfigs() {
    clientStore.saveConfigs();
}
</script>
<template>
    <div class="r">
        <Flex gap="large">
            <VAceEditor
                class="editor"
                v-model:value="clientStore.configs"
                mode="json"
                theme="chrome"
            />
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
                    <TypographyTitle :level="5"
                        >Api和Client配置:</TypographyTitle
                    >
                    <TypographyParagraph
                        >载入示例配置:
                        <Button
                            size="small"
                            @click="clientStore.loadExample"
                            :icon="h(UploadOutlined)"
                        ></Button
                    ></TypographyParagraph>
                    <TypographyTitle :level="5"
                        >LLM的Api和Client</TypographyTitle
                    >
                    <TypographyParagraph
                        >https://ollama.com/library</TypographyParagraph
                    >
                </Flex>
            </Flex>
        </Flex>
    </div>
</template>
<style scoped>
.r {
    margin-left: 2em;
    margin-right: 2em;
    overflow: auto;
}
.editor {
    width: 50vw;
    height: 80vh;
}
</style>
