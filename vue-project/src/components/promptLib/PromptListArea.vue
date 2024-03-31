<script setup lang="ts">
import { usePromptLibStore } from "@/stores/promptLibStore";
import { Flex, TypographyTitle, List } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { ref } from "vue";
const promptLibStore = usePromptLibStore()
const {prompts} = storeToRefs(promptLibStore);

</script>
<template>
    <Flex vertical gap="middle" class="list">
        <TypographyTitle :level="5">Prompts</TypographyTitle>
        <List size="large" bordered :dataSource="prompts">
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
