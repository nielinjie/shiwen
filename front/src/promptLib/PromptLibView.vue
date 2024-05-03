<script setup lang="ts">
import { Flex } from "ant-design-vue";
import PromptSearchingArea from "./PromptSearchingArea.vue";
import PromptSeedArea from "./PromptSeedArea.vue";
import PromptListArea from "./PromptListArea.vue";
import { usePromptLibStore } from "./promptLibStore";
const libStore = usePromptLibStore();
libStore.searchPrompt();
libStore.loadSources();
</script>
<template>
    <div class="r">
    <Flex vertical gap="large" class="content">
        <PromptSearchingArea></PromptSearchingArea>
        <Flex gap="large">
            <PromptSeedArea></PromptSeedArea>
            <PromptListArea></PromptListArea>
        </Flex>
    </Flex>
</div>
</template>
<style scoped>
.r {
    margin-left: 2em;
    margin-right: 2em;
    overflow: auto;
}
</style>
@/promptLib/promptLibStore