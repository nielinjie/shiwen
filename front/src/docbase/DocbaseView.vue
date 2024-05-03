<script setup lang="ts">
import DocListArea from "./DocListArea.vue";
import RefDocListArea from "./RefDocListArea.vue";
import JointContentArea from "./JointContentArea.vue";
import SearchingArea from "./SearchingArea.vue";
import BaseInfoArea from "./BaseInfoArea.vue";
import { Flex } from "ant-design-vue";
</script>
<template>
    <div class="r">
        <Flex gap="large">
            <Flex vertical gap="large">
                <BaseInfoArea></BaseInfoArea>
                <RefDocListArea></RefDocListArea>
            </Flex>
            <Flex vertical gap="large">
                <DocListArea></DocListArea>
                <!-- <DocDetailArea></DocDetailArea> -->
            </Flex>
            <Flex vertical gap="large">
                <SearchingArea></SearchingArea>
                <JointContentArea></JointContentArea>
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
