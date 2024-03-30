<script setup lang="ts">
import { TypographyTitle, List, Flex, Checkbox } from "ant-design-vue";
import { ref } from "vue";

const data = ref([
    { name: "本地存储", description: "本地存储的种子", checked: true },
    { name: "云端存储", description: "云端存储的种子", checked: false },
]);
const externalData = ref([
    { name: "Seed 1", description: "Seed 1 description", checked: false },
    { name: "Seed 2", description: "Seed 2 description", checked: false },
    { name: "Seed 3", description: "Seed 3 description", checked: false },
]);
</script>
<template>
    <Flex vertical gap="middle" class="seeds">
        <TypographyTitle :level="5">Prompt Seeds: </TypographyTitle>
        <List size="large" bordered :dataSource="data">
            <template #renderItem="{ item, index }">
                <SeedListItem
                    :item="item"
                    @changed="(e:boolean)=>data[index].checked= e"
                ></SeedListItem>
            </template>
        </List>
        <List size="large" bordered :dataSource="externalData">
            <template #renderItem="{ item, index }">
                <SeedListItem
                    :item="item"
                    @changed="(e:boolean)=>externalData[index].checked= e"
                ></SeedListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped>
.seeds {
    width: 30vw;
    height: 90vh;
}
</style>
