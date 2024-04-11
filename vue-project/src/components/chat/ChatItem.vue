<script setup lang="ts">
import { Flex, ListItem, TypographyText, Avatar } from "ant-design-vue";
import { UserOutlined, CustomerServiceOutlined } from "@ant-design/icons-vue";
const props = defineProps<{
    message: String;
    sender: String;
}>();
</script>
<template>
    <ListItem :class="props.sender == 'User' ? 'right' : 'left'">
        <Flex gap="middle" v-if="props.sender == 'User'">
            <TypographyText>{{ props.message }}</TypographyText>
            <TypographyText strong>
                {{ props.sender }}
            </TypographyText>
            <Avatar size="small">
                <template #icon>
                    <UserOutlined />
                </template>
            </Avatar>
        </Flex>
        <Flex gap="middle" v-else>
            <Avatar size="small">
                <template #icon> <CustomerServiceOutlined /> </template>
            </Avatar>
            <TypographyText strong>
                {{ props.sender }}
            </TypographyText>
            <TypographyText>{{ props.message }}</TypographyText>
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
</style>
