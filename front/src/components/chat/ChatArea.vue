<script setup lang="ts">
import { useChatStore } from "@/stores/chatStore";
import { storeToRefs } from "pinia";
import { ref, watch, nextTick } from "vue";
import { List, Flex, Input, Button, TypographyTitle } from "ant-design-vue";
const chatStore = useChatStore();
const { history, messageInputting } = storeToRefs(chatStore);

const chatList = ref<any>(null);
watch([history,history.value], () => {
    nextTick(() => {
        if (chatList.value != null) {
            chatList.value.scrollTop = chatList.value?.scrollHeight;
        }
    });
});
function send() {
    chatStore.sendMessage();
}
function nothing(e: any) {
    e.preventDefault();
}
</script>
<template>
    <div>
        <Flex vertical gap="large">
            <TypographyTitle :level="3">Chat Here: </TypographyTitle>
            <div class="chatList" ref="chatList">
                <List size="large" bordered :dataSource="history">
                    <template #renderItem="{ item }">
                        <ChatItem
                            :sender="item.sender"
                            :message="item.content"
                        ></ChatItem>
                    </template>
                </List>
            </div>
            <Flex gap="middle">
                <Input
                    v-model:value="messageInputting"
                    @keydown.native.enter="send"
                    @keydown.native.229.="nothing"
                    size="large"
                ></Input>
                <Button size="large" @click="send">Send</Button>
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
