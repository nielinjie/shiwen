<script setup lang="ts">
import { useDocbaseStore } from "./docbaseStore";
import { storeToRefs } from "pinia";

import { Flex, TypographyTitle } from "ant-design-vue";
import { computed } from "vue";
import { VAceEditor } from "vue3-ace-editor";

const { docDetail } = storeToRefs(useDocbaseStore());
const docString = computed(() => JSON.stringify(docDetail.value, null, 2));
</script>
<template>
    <Flex vertical gap="middle" class="r">
        <TypographyTitle :level="3">文档信息：</TypographyTitle>
        <div class="editor">
            <VAceEditor
                v-model:value="docString"
                :options="{ showGutter: false, wrap: true }"
                :readonly="true"
                mode="json"
                theme="chrome"
            />
        </div>
    </Flex>
</template>
<style scoped>
.r {
    min-width: 25vw;
}
.editor {
    width: 25vw;
    height: 30vh;
}
.editor .ace_editor {
    min-height: 100%;
}
</style>
