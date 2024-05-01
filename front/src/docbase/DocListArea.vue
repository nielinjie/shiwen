<script setup lang="ts">
import { useDocbaseStore, type DocInfo } from "./docbaseStore";
import { ListItem,  TypographyTitle,Flex,List,TypographyText} from "ant-design-vue";
import { storeToRefs } from "pinia";

const docStore = useDocbaseStore();
const { referringDocs ,derivedDocs,condition} = storeToRefs(docStore);

function selectRefDoc(doc:DocInfo){
    docStore.selectRefDoc(doc);
}
function selectDerDoc(doc:DocInfo){
    docStore.selectDerDoc(doc);
}
</script>
<template>
    <Flex gap="middle" vertical class="r">
        <TypographyTitle :level="3">文档片：</TypographyTitle>
        <TypographyText>{{ condition }}</TypographyText>
        <List size="large" bordered :dataSource="derivedDocs">
            <template #renderItem="{ item }"> 
                <ListItem @click="selectDerDoc(item)" :item="item" >{{ item.id }}</ListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped>
.r {
    min-width: 30vw;
    min-height: 70vh;
}
</style>