<script setup lang="ts">
import { Flex, Input } from "ant-design-vue";
import { ref } from "vue";
import { usePromptLibStore } from "./promptLibStore";
import { storeToRefs } from "pinia";
const inputting = ref("");
const promptStore = usePromptLibStore();
const { query } = storeToRefs(promptStore);
inputting.value = query.value
function search() {
    query.value = inputting.value;
    promptStore.searchPrompt();
}
</script>
<template>
    <Flex justify="space-around">
        <Input
            v-model:value="inputting"
            size="large"
            class="input"
            allowClear
            placeholder="Search Prompts ..."
            @keydown.enter="search"
        />
    </Flex>
</template>
<style scoped>
.input {
    width: 50vw;
}
</style>
@/promptLib/promptLibStore