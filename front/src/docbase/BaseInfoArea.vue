<script setup lang="ts">
import { useDocbaseStore } from "./docbaseStore";
import { useBaseInfoStore } from "./baseInfoStore";
import { storeToRefs } from "pinia";
import { VAceEditor } from "vue3-ace-editor";

import {
    TypographyTitle,
    Flex,
    TypographyText,
    Collapse,
    CollapsePanel,
    Button,
    Select,
    SelectOption,
} from "ant-design-vue";
import { ref, watch } from "vue";
const baseStore = useDocbaseStore();
const { baseInfo } = storeToRefs(baseStore);
const fileLocationListString = ref("");
const baseInfoStore = useBaseInfoStore();
const { manualIndexers, manualIndexerSelected, fileLocationList } =
    storeToRefs(baseInfoStore);
watch(
    fileLocationList.value,
    (newVal: any) => {
        fileLocationListString.value = JSON.stringify(newVal,null,2);
    },
    { immediate: true }
);
watch(
    fileLocationList,
    (newVal: any) => {
        fileLocationListString.value = JSON.stringify(newVal,null,2);
    },
    { immediate: true }
);
function saveFileLocationList() {
    fileLocationList.value = JSON.parse(fileLocationListString.value);
    baseInfoStore.saveFileLocationList();
}
function index() {
    baseInfoStore.index();
}
</script>
<template>
    <Flex vertical gap="large" class="rr">
    <TypographyTitle :level="3">文档库：</TypographyTitle>
    <Collapse>
        <CollapsePanel header="基本信息" key="1">
            <VAceEditor
                v-model:value="baseInfo"
                :options="{ showGutter: false, wrap: true }"
                :readonly="true"
                mode="json"
            />
        </CollapsePanel>
        <CollapsePanel header="文档地址" key="2">
            <VAceEditor
                v-model:value="fileLocationListString"
                :options="{ showGutter: false, wrap: true }"
                mode="json"
            />
            <Button @click="saveFileLocationList()">Save</Button>
        </CollapsePanel>
        <CollapsePanel header="进行索引" key="3">
            <Flex gap="large">
                <Select v-model:value="manualIndexerSelected">
                    <SelectOption v-for="sm in manualIndexers" :value="sm">{{
                        sm
                    }}</SelectOption>
                </Select>
                <Button @click="index()">Index</Button>
            </Flex>
        </CollapsePanel>
    </Collapse>
</Flex>
</template>
<style scoped>
.rr {
    min-width: 30vw;
    min-height: 30vh;
}

.ace_editor {
    height: 25vh;
    width: 25vw;
}
</style>
