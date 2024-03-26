<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Space, Flex ,TypographyText} from "ant-design-vue";

const workspaceStore = useWorkspaceStore();
</script>
<template>
    <Flex :vertical="true" justify="flex-start" gap="large">
        <TypographyText strong>Workspace State:</TypographyText>
        <Flex gap="middle">
            <Button @click="workspaceStore.save">Save</Button>
            <Button @click="workspaceStore.load">Load</Button>
        </Flex>
        
    </Flex>
</template>
