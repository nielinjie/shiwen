<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Space, Flex ,TypographyTitle} from "ant-design-vue";

const workspaceStore = useWorkspaceStore();
</script>
<template>
    <Flex :vertical="true" justify="flex-start" gap="large">
        <TypographyTitle :level="5">Workspace State:</TypographyTitle>
        <Flex gap="middle">
            <Button @click="workspaceStore.save">Save</Button>
            <Button @click="workspaceStore.load">Load</Button>
        </Flex>
        
    </Flex>
</template>
