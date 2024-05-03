<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Flex, TypographyText } from "ant-design-vue";
import { h } from "vue";
import { SaveOutlined, UploadOutlined } from "@ant-design/icons-vue";

const workspaceStore = useWorkspaceStore();
</script>
<template>
    <Flex :vertical="true" justify="flex-start" gap="large">
        <TypographyText strong>Workspace State:</TypographyText>
        <Flex gap="middle">
            <Button @click="workspaceStore.save" :icon="h(SaveOutlined)"
                >Save</Button
            >
            <Button @click="workspaceStore.load" :icon="h(UploadOutlined)"
                >Load</Button
            >
        </Flex>
    </Flex>
</template>
@/matrix/workspaceStore../stores/workspaceStore