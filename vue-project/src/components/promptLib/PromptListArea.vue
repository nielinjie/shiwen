<script setup lang="ts">
import { Flex, TypographyTitle, List } from "ant-design-vue";
import { ref } from "vue";
const data = ref([
    { title: "Prompt 1", content: "Prompt 1 content", seedName: "本地存储" },
    { title: "Prompt 2", content: "Prompt 2 content", seedName: "本地存储" },
    { title: "Prompt 3", content: "Prompt 3 content", seedName: "本地存储" },
    { title: "Prompt 2", content: "Prompt 2 content", seedName: "云端存储" },
    { title: "Prompt 3", content: "Prompt 3 content", seedName: "云端存储" },
    { title: "Prompt 3", content: "Prompt 3 content", seedName: "Seed1" },
]);
</script>
<template>
    <Flex vertical gap="middle" class="list">
        <TypographyTitle :level="5">Prompts</TypographyTitle>
        <List size="large" bordered :dataSource="data">
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
