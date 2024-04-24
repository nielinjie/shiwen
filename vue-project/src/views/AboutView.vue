<script setup lang="ts">
import { List, ListItem, Button } from "ant-design-vue";
import { useMessageStore } from "@/stores/messageStore";
import { storeToRefs } from "pinia";
const store = useMessageStore();
const { messages } = storeToRefs(store);
</script>

<template>
    <List size="large" bordered :dataSource="messages">
        <template #renderItem="{ item }">
            <ListItem :item="item">{{ item }}</ListItem>
        </template>
    </List>
    <Button @click="store.sendMessage('/app/hello',{ name: 'foo' })"
        >Add Message</Button
    >
</template>

<style>
@media (min-width: 1024px) {
    .about {
        min-height: 100vh;
        display: flex;
        align-items: center;
    }
}
</style>
