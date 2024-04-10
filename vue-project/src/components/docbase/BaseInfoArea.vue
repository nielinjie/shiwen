<script setup lang="ts">
import { useDocbaseStore } from "@/stores/docbaseStore";
import { storeToRefs } from "pinia";
import { TypographyTitle, Flex, TypographyText } from "ant-design-vue";
const baseStore = useDocbaseStore();
const { baseInfo } = storeToRefs(baseStore);
</script>
<template>
    <Flex vertical gap="middle" class="baseInfo">
        <TypographyTitle :level="3">DocBase Info -</TypographyTitle>
        <TypographyText>{{ baseInfo }} </TypographyText>
    </Flex>
</template>
<style scoped>
.baseInfo {
    min-width: 30vw;
}
</style>
