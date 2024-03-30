<script setup lang="ts">
import { TypographyTitle, List ,Flex} from "ant-design-vue";
const data = ["本地保存", "云端保存..."];
</script>
<template>
        <Flex vertical gap="middle" class="seeds">
            <TypographyTitle :level="5">Prompt Seeds: </TypographyTitle>
            <List size="large" bordered :dataSource="data">
                <template #renderItem="{ item }">
                    <List.Item>{{ item }}</List.Item>
                </template>
            </List>
        </Flex>
</template>
<style scoped>
.seeds {
    width: 30vw;
}
</style>
