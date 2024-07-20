<script setup lang='ts'>
import { watch } from 'vue';
import { ref } from 'vue'
import { List,ListItem,Checkbox } from 'ant-design-vue';
import {useFunctionsStore, type Fun} from './functionsStore'
import { storeToRefs } from 'pinia';
export interface FunctionSelected{
    name:string
    description:string
    checked:boolean
}
const funStore  = useFunctionsStore()
const {functions } = storeToRefs(funStore)
    const functionsSelected = ref<FunctionSelected[]>([])
    watch(functions, (newVal) => {
        functionsSelected.value = (newVal as Fun[]).map((fun) => ({
            name: fun.name,
            description: fun.description,
            checked: false,
        }));
    }, { immediate: true })
</script>
<template>
    <List size="large" bordered :dataSource="functionsSelected">
            <template #renderItem="{ item }">
                <ListItem :item="item">
                    <Checkbox v-model:checked="item.checked"></Checkbox>
                    {{ item.name }} - {{ item.description }}
                </ListItem>
            </template>
        </List>
</template>
<style scoped>

</style>