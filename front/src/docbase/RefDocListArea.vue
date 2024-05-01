<script setup lang='ts'>
    import { useDocbaseStore, type DocInfo } from "./docbaseStore";
import { ListItem,  List,Flex,TypographyTitle} from "ant-design-vue";
import { storeToRefs } from "pinia";

const docStore = useDocbaseStore();
const { referringDocs} = storeToRefs(docStore);

function selectRefDoc(doc:DocInfo){
    docStore.selectRefDoc(doc);
}
</script>
<template>
    <Flex vertical gap="large">
                <TypographyTitle :level="4">原始文件：</TypographyTitle>

    <List size="large" bordered :dataSource="referringDocs">
            <template #renderItem="{ item }"> 
                <ListItem @click="selectRefDoc(item)" :item="item" >{{ item.id }}</ListItem>
            </template>
        </List>
    </Flex>
</template>
<style scoped>

</style>