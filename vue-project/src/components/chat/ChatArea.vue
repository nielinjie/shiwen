<script setup lang="ts">
import { useChatStore } from "@/stores/chatStore";
import { storeToRefs } from "pinia";
import { ref, watch,nextTick } from "vue";
import {
    List,
    ListItem,
    Flex,
    TypographyText,
    Input,
    Button,
    TypographyTitle,
    message,
} from "ant-design-vue";
import type { Message } from "@/stores/socketStore";
const chatStore = useChatStore();
const { history, messageInputting } = storeToRefs(chatStore);
export interface MessageItemDisplay {
    message: string;
    sender: string;
}
const messagesHistory = ref<MessageItemDisplay[]>([
    { message: "Welcome to the chat!", sender: "Bot" },
    { message: "Hello There", sender: "User" },
]);
watch(history.value, (newVal) => {
    console.log("history changing");
    messagesHistory.value = newVal.map((message: Message) => ({
        message: message.content,
        sender: message.direct === "down" ? "Bot" : "User",
    }));
});
const chatList = ref<any>(null);
watch(messagesHistory, () => {
    nextTick(() => {
        if (chatList.value != null) {
            chatList.value.scrollTop = chatList.value?.scrollHeight;
        }
    });
});
</script>
<template>
    <div>
        <Flex vertical gap="large">
            <TypographyTitle :level="3">Chat Here: </TypographyTitle>
            <div class="chatList" ref="chatList">
                <List size="large" bordered :dataSource="messagesHistory">
                    <template #renderItem="{ item }">
                        <ChatItem
                            :sender="item.sender"
                            :message="item.message"
                        ></ChatItem>
                    </template>
                </List>
            </div>
            <Flex gap="middle">
                <Input v-model:value="messageInputting" size="large"></Input>
                <Button size="large" @click="chatStore.sendMessage()"
                    >Send</Button
                >
            </Flex>
        </Flex>
    </div>
</template>
<style scoped>
.chatList {
    width: 70vw;
    height: 50vh;
    overflow: scroll;
}
</style>
