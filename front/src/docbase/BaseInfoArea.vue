<script setup lang="ts">
import { useDocbaseStore } from "./docbaseStore";
import { storeToRefs } from "pinia";
import { VAceEditor } from "vue3-ace-editor";

import { TypographyTitle, Flex, TypographyText } from "ant-design-vue";
const baseStore = useDocbaseStore();
const { baseInfo } = storeToRefs(baseStore);
</script>
<template>
    <Flex vertical gap="middle" class="baseInfo">
        <TypographyTitle :level="3">文档库：</TypographyTitle>
        <div class="editor">
            <VAceEditor
                v-model:value="baseInfo"
                :options="{ showGutter: false, wrap: true }"
                :readonly="true"
                mode="json"
            />
        </div>
    </Flex>
</template>
<style scoped>
.baseInfo {
    min-width: 30vw;
    min-height: 30vh;
}
.editor {
    width: 30vw;
    height: 30vh;
}
.editor .ace_editor {
    min-height: 100%;
}
</style>
