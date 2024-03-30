<script setup lang="ts">
import { List, Flex, Checkbox } from "ant-design-vue";
export interface PromptSeed {
    id:string,
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
        <Flex gap="large">
            <div>{{ props.item.name }}</div>
            <Checkbox
                :checked="props.item.checked"
                @click="$emit('changed', !(props.item.checked))"
            >
            </Checkbox> </Flex
    ></List.Item>
</template>
<style scoped></style>
