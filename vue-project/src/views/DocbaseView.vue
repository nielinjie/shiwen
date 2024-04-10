<script setup lang="ts">
import DocListArea from "@/components/docbase/DocListArea.vue";
import DocDetailArea from "@/components/docbase/DocDetailArea.vue";
import BaseInfoArea from "@/components/docbase/BaseInfoArea.vue";
import { Flex } from "ant-design-vue";
</script>
<template>
    <div class="r">
        <Flex gap="large">
            <BaseInfoArea></BaseInfoArea>
            <DocListArea></DocListArea>
            <Flex vertical gap="large">
                <SearchingArea></SearchingArea>
                <DocDetailArea></DocDetailArea>
            </Flex>
        </Flex>
    </div>
</template>
<style scoped>
.r {
    margin-left: 2em;
    margin-right: 2em;
    overflow: auto;
}
</style>
