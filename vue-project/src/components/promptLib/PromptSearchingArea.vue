<script setup lang="ts">
import { Flex ,Input} from "ant-design-vue";
</script>
<template>
    <Flex justify="space-around">
        <Input size="large" class="input" allowClear placeholder="Search Prompts ..."/>
    </Flex>
</template>
<style scoped>
.input {
    width: 50vw;
}
</style>