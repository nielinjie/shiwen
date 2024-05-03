<script setup lang="ts">
import { useDocbaseStore, type DocObject } from "./docbaseStore";
import { storeToRefs } from "pinia";
import { watch } from "vue";

import {
    Flex,
    TypographyTitle,
    Checkbox,
    List,
    ListItem,
    Button,
} from "ant-design-vue";
import { ref } from "vue";

const { docDetails } = storeToRefs(useDocbaseStore());
const docWithSelected = ref<
    { id: string; detail: DocObject; checked: boolean }[]
>([]);
watch(
    docDetails, //仅被set value的时候需要监听？
    (newVal) => {
        docWithSelected.value = newVal.map((doc) => ({
            id: doc.id,
            detail: doc,
            checked: false,
        }));
        console.log(docWithSelected.value);
    },
    { immediate: true }
);
function send() {
    const selectedDocs = docWithSelected.value.filter((doc) => doc.checked);
    console.log(selectedDocs.map((it) => it.detail.content));
}
</script>
<template>
    <Flex vertical gap="middle" class="r">
        <TypographyTitle :level="3">内容：</TypographyTitle>
        <Flex gap="middle">
            <Button size="small" @click="send"> Add to Matrix</Button></Flex
        >
        <List size="large" bordered :dataSource="docWithSelected">
            <template #renderItem="{ item }">
                <ListItem :item="item">
                    <Checkbox v-model:checked="item.checked"></Checkbox>
                    {{ item.detail.content }}
                </ListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped>
.r {
    min-width: 25vw;
}
.editor {
    width: 25vw;
    height: 30vh;
}
</style>
