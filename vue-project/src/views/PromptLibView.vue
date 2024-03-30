<script setup lang="ts">
import { Flex } from "ant-design-vue";
import PromptSearchingArea from "../components/promptLib/PromptSearchingArea.vue";
import PromptSeedArea from "../components/promptLib/PromptSeedArea.vue";
import PromptListArea from "../components/promptLib/PromptListArea.vue";
</script>
<template>
    <Flex vertical gap="large" class="content">
        <PromptSearchingArea></PromptSearchingArea>
        <Flex gap="large">
            <PromptSeedArea></PromptSeedArea>
            <PromptListArea></PromptListArea>
        </Flex>
    </Flex>
</template>
<style scoped>
.content {
    margin-left: 2em;
    margin-right: 2em;
}
</style>

