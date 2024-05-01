<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useChatStore } from "@/stores/chatStore";
import { Input, Button, Flex } from "ant-design-vue";
import  ChatArea  from "@/components/chat/ChatArea.vue";
import  LogArea  from "@/components/chat/LogArea.vue";
</script>
<template>
    <div class="r">
        <Flex gap="large">
            <ChatArea></ChatArea>
            <LogArea></LogArea>
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
