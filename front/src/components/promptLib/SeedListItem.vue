<script setup lang="ts">
import { List, Flex, Checkbox, TypographyText } from "ant-design-vue";
export interface PromptSeed {
    id: string;
    name: string;
    description: string;
    checked: boolean;
}
const props = defineProps<{
    item: PromptSeed;
}>();
</script>
<template>
    <List.Item>
        <Flex vertical gap="middle">
            <Flex gap="large">
                <TypographyText strong>{{ props.item.name }}</TypographyText>
                <Checkbox
                    :checked="props.item.checked"
                    @click="$emit('changed', !props.item.checked)"
                >
                </Checkbox> </Flex
            ><TypographyText>
                {{ props.item.description }}
            </TypographyText>
        </Flex>
    </List.Item>
</template>
<style scoped></style>
