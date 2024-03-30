<script setup lang="ts">
import { List, Flex, Checkbox, Button, TypographyText } from "ant-design-vue";
import {PlayCircleOutlined,CopyOutlined} from "@ant-design/icons-vue";
import { ref } from "vue";
export interface PromptObject {
    id: string;
    seedName:String
    title: string;
    content: string;
}
const props = defineProps<{
    item: PromptObject;
}>();
const hover = ref(false);
</script>
<template>
    <List.Item @mouseover="hover = true" @mouseout="hover = false">
        <Flex vertical gap="middle">
            <Flex gap="large">
                <Flex gap="large">
                <TypographyText strong>
                    {{ props.item.title }}
                </TypographyText>
                <TypographyText type="secondary" size="small">
                    {{ props.item.seedName }}
                </TypographyText>
                </Flex>
                <Flex v-visibility="hover" :gap="2">
                    <Button size="small" shape="circle"><PlayCircleOutlined /></Button>
                    <Button size="small" shape="circle"><CopyOutlined /></Button>
                </Flex>
            </Flex>
            <Flex>{{ props.item.content }}</Flex>
        </Flex></List.Item
    >
</template>
<style scoped></style>
