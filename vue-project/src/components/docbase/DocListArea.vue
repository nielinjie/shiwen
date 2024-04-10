<script setup lang="ts">
import { useDocbaseStore, type DocInfo } from "@/stores/docbaseStore";
import { ListItem,  TypographyTitle,Flex,List} from "ant-design-vue";
import { storeToRefs } from "pinia";

const docStore = useDocbaseStore();
const { docs } = storeToRefs(docStore);
function selectDoc(doc:DocInfo){
    docStore.selectDoc(doc);
}
</script>
<template>
    <Flex gap="middle" vertical>
        <TypographyTitle :level="3">Docs</TypographyTitle>
        <List size="large" bordered :dataSource="docs">
            <template #renderItem="{ item }"> 
                <ListItem @click="selectDoc(item)" :item="item" >{{ item.id }}</ListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped></style>
