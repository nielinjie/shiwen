<script setup lang="ts">
import { useDocbaseStore } from "@/stores/docbaseStore";
import { storeToRefs } from "pinia";
import { Flex, TypographyTitle,TypographyText } from "ant-design-vue";

const { docDetail } = storeToRefs(useDocbaseStore());
</script>
<template>
    <Flex vertical gap="middle" class="r">
        <TypographyTitle :level="3">Doc Detail - </TypographyTitle>
        <TypographyTitle :level="5">{{
            docDetail?.name ?? ""
        }}</TypographyTitle>
        <TypographyText>{{
            docDetail?.content ?? ""
        }}</TypographyText>
    </Flex>
</template>
<style scoped>
.r {
    min-width: 30vw;
}
</style>
