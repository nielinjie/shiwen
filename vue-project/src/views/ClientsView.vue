<script setup lang="ts">
import { useClientStore } from "@/stores/clientStore";
import { Button, Row } from "ant-design-vue";
import JsonEditorVue from "json-editor-vue";
const clientStore = useClientStore();
function saveConfigs() {
    clientStore.saveConfigs();
}
</script>
<template>
    <Row>
        <JsonEditorVue
            :content="{ text: clientStore.configs }"
            :onChange="(t) => (clientStore.configs = t.text)"
        >
        </JsonEditorVue>
    </Row>
    <Row>
        <Button @click="saveConfigs">
            <span>Save</span>
        </Button>
    </Row>
</template>
