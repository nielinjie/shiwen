<script setup lang="ts">
import { nextTick, ref, watch } from "vue";
import { storeToRefs } from "pinia";
import { List, Flex, TypographyTitle, ListItem } from "ant-design-vue";

import { useLogStore, type LogMessage } from "@/stores/logStore";
import { TypographyText } from "ant-design-vue";

export interface MessageItemDisplay {
    message: string;
    level: "info" | "error" | "warn";
}

const logStore = useLogStore();
const { socketMessages } = storeToRefs(logStore);
const messagesHistory = ref<MessageItemDisplay[]>([]);
watch(socketMessages.value, (newVal) => {
    messagesHistory.value = [
        ...newVal.map((message: LogMessage) => ({
            message: message.message,
            level: message.level ?? "info",
        })),
    ];
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
            <TypographyTitle :level="5">Log Here: </TypographyTitle>
            <div class="chatList" ref="chatList">
                <List size="large" bordered :dataSource="messagesHistory">
                    <template #renderItem="{ item }">
                        <ListItem>
                            <TypographyText>{{ item.message }}</TypographyText>
                        </ListItem>
                    </template>
                </List>
            </div>
        </Flex>
    </div>
</template>
<style scoped>
.chatList {
    width: 20vw;
    height: 50vh;
    overflow: scroll;
}</style>
