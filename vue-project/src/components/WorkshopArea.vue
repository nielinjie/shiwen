<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Space } from "ant-design-vue";

const workspaceStore = useWorkspaceStore();
</script>
<template>
    <Space>
        <Button @click="workspaceStore.save">Save</Button>
        <Button @click="workspaceStore.load">Load</Button>
    </Space>
</template>
