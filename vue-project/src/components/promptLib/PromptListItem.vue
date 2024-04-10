<script setup lang="ts">
import { List, Flex, Button, TypographyText } from "ant-design-vue";
import { PlayCircleOutlined, CopyOutlined } from "@ant-design/icons-vue";
import { ref } from "vue";
import type { PromptObject } from "./PromptListArea.vue";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRouter } from "vue-router";
const workspaceStore = useWorkspaceStore();
const props = defineProps<{
    item: PromptObject;
}>();
const hover = ref(false);
const route = useRouter();
function addToWorkspace() {
    workspaceStore.addPrompt(props.item.content); //TODO add prompt with title/content and meta. connect matrix with lib.
    route.push("/");
}
</script>
<template>
    <List.Item @mouseover="hover = true" @mouseout="hover = false">
        <Flex vertical gap="middle">
            <Flex gap="large">
                <Flex gap="large">
                    <TypographyText strong>
                        {{ props.item.title }}
                    </TypographyText>
                    <TypographyText type="secondary" size="small">
                        {{ props.item.sourceName }}
                    </TypographyText>
                </Flex>
                <Flex v-visibility="hover" :gap="2">
                    <Button size="small" shape="circle" @click="addToWorkspace"
                        ><PlayCircleOutlined
                    /></Button>
                    <Button size="small" shape="circle"
                        ><CopyOutlined
                    /></Button>
                </Flex>
            </Flex>
            <Flex>{{ props.item.content }}</Flex>
        </Flex></List.Item
    >
</template>
<style scoped></style>
