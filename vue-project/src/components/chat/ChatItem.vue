<script setup lang="ts">
import { Flex, ListItem, TypographyText, Avatar } from "ant-design-vue";
import { UserOutlined, CustomerServiceOutlined } from "@ant-design/icons-vue";
import ChatMessage from "./ChatMessage.vue";
const props = defineProps<{
    message: { type: string; body: string };
    sender: string;
}>();
const messageType = props.message.type;
</script>
<template>
    <ListItem :class="props.sender == 'User' ? 'right' : 'left'">
        <Flex gap="middle" v-if="props.sender == 'User'">
            <ChatMessage :message="props.message" :type="messageType" />
            <Flex class="header" gap="middle">
                <TypographyText strong>
                    {{ props.sender }}
                </TypographyText>
                <Avatar size="small">
                    <template #icon>
                        <UserOutlined />
                    </template>
                </Avatar>
            </Flex>
        </Flex>
        <Flex gap="middle" v-else>
            <Flex gap="middle" class="header">
                <Avatar size="small">
                    <template #icon> <CustomerServiceOutlined /> </template>
                </Avatar>
                <TypographyText strong>
                    {{ props.sender }}
                </TypographyText>
            </Flex>
            <ChatMessage :message="props.message" :type="messageType" />
        </Flex>
    </ListItem>
</template>
<style scoped>
.right {
    justify-content: flex-end;
}
.left {
    justify-content: start;
}
.header {
    min-width: 75px;
}
</style>
