<script setup lang="ts">
import { usePromptLibStore } from "./promptLibStore";
import { TypographyTitle, List, Flex, Checkbox } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { ref } from "vue";
import SeedListItem from "./SeedListItem.vue"
const sourceStore = usePromptLibStore();
const { storages, seeds } = storeToRefs(sourceStore);



</script>
<template>
    <Flex vertical gap="middle" class="seeds">
        <TypographyTitle :level="5">Prompt Source: </TypographyTitle>
        <List size="large" bordered :dataSource="storages">
            <template #renderItem="{ item, index }">
                <SeedListItem
                    :item="item"
                    @changed="(e:boolean)=>storages[index].checked= e"
                ></SeedListItem>
            </template>
        </List>
        <List size="large" bordered :dataSource="seeds">
            <template #renderItem="{ item, index }">
                <SeedListItem
                    :item="item"
                    @changed="(e:boolean)=>seeds[index].checked= e"
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
@/promptLib/promptLibStore