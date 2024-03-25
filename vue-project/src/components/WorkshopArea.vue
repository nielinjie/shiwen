<script setup lang="ts">
import { useClientStore } from "@/stores/clientStore";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Space, Modal } from "ant-design-vue";
import { ref, type Ref } from "vue";
import JsonEditorVue from "json-editor-vue";

const workspaceStore = useWorkspaceStore();
const clientStore = useClientStore();
const displayConfig: Ref<boolean> = ref(false);
function saveConfigs() {
    clientStore.saveConfigs();
    displayConfig.value = false;
}
</script>
<template>
    <Space>
        <Button @click="workspaceStore.save">Save</Button>
        <Button @click="workspaceStore.load">Load</Button>
        <Button @click="displayConfig = true">ClientConfigs</Button>
    </Space>
    <Modal
        v-model:open="displayConfig"
        title="Client Configs"
        @ok="saveConfigs"
    >
        <JsonEditorVue
            :content="{ text: clientStore.configs }"
            :onChange="(t) => (clientStore.configs = t.text)"
        >
        </JsonEditorVue>
    </Modal>
</template>
