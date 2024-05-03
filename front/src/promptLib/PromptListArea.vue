<script setup lang="ts">
import { usePromptLibStore, type Prompt } from "./promptLibStore";
import { Flex, TypographyTitle, List } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { computed, type Ref } from "vue";
import PromptListItem from "./PromptListItem.vue";

export interface PromptObject {
    id: string;
    sourceName: String;
    title: string;
    content: string;
}

const promptLibStore = usePromptLibStore();
const { prompts, sourcesIdToName } = storeToRefs(promptLibStore);
const promptObjs: Ref<PromptObject[]> = computed(() =>
    prompts.value.map((prompt: Prompt) => ({
        sourceName: sourcesIdToName.value[prompt.sourceId] ?? "Unknown",
        ...prompt,
    }))
);
</script>
<template>
    <Flex vertical gap="middle" class="list">
        <TypographyTitle :level="5">Prompts</TypographyTitle>
        <List size="large" bordered :dataSource="promptObjs">
            <template #renderItem="{ item }">
                <PromptListItem :item="item"></PromptListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped>
.list {
    width: 70vw;
}
</style>
@/promptLib/promptLibStore