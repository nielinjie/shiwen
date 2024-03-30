<script setup lang="ts">
import { Flex, TypographyTitle, List } from "ant-design-vue";
const data = ["Prompt 1", "Prompt 2", "Prompt 3"];
</script>
<template>
    <Flex vertical gap="middle" class="list">
        <TypographyTitle :level="5">Prompts</TypographyTitle>
        <List size="large" bordered :dataSource="data">
            <template #renderItem="{ item }">
                <List.Item>{{ item }}</List.Item>
            </template>
        </List>
    </Flex>
</template>
<style scoped>
.list {
    width: 70vw;
}
</style>
