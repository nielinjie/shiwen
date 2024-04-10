<script setup lang="ts">
import { useDocbaseStore } from "@/stores/docbaseStore";
import { Flex, Textarea,TypographyTitle,Select,SelectOption } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { ref } from "vue";
const searchInputting = ref("");
const docbaseStore=useDocbaseStore()
const {searching ,searchMethods,searchMethod}= storeToRefs(docbaseStore)
searchInputting.value = searching.value
function search() {
    searching.value = searchInputting.value;
    docbaseStore.searchDoc();
}
</script>
<template>
    <div class="r">
        <Flex vertical gap="large">
            <TypographyTitle :level="3">Searching - </TypographyTitle>
            <Textarea v-model:value="searchInputting"></Textarea>
            <Select v-model:value="searchMethod">
                <SelectOption v-for="sm in searchMethods" :value = "sm.value" >{{ sm.text }}</SelectOption>
            </Select>
        </Flex>
    </div>
</template>
<style scoped>
.r {
    min-height: 30vh;
}
</style>
