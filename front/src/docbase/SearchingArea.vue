<script setup lang="ts">
import {useSearchingStore} from "./searchingStore";
import { Flex, Textarea,TypographyTitle,Select,Button,SelectOption } from "ant-design-vue";
import { storeToRefs } from "pinia";
const searchingStore=useSearchingStore()
const {searchInputting ,searchMethods,searchMethodSelected}= storeToRefs(searchingStore)
function search(){
    searchingStore.search()
}
</script>
<template>
    <div class="r">
        <Flex vertical gap="large">
            <TypographyTitle :level="3">检索： </TypographyTitle>
            <Textarea v-model:value="searchInputting"></Textarea>
            <Select v-model:value="searchMethodSelected">
                <SelectOption v-for="sm in searchMethods" :value = "sm" >{{ sm }}</SelectOption>
            </Select>
            <Button @click="search()">Search</Button>
        </Flex>
    </div>
</template>
<style scoped>
.r {
    min-height: 30vh;
}
</style>
@/docbase/docbaseStore