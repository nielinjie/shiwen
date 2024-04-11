<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useChatStore } from "@/stores/chatStore";
import { Input, Button, Flex } from "ant-design-vue";
</script>
<template>
    <div class="r">
        <Flex gap="large">
            <ChatArea></ChatArea>
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
