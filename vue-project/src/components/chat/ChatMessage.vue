<script setup lang="ts">
import { defineProps } from "vue";
import MarkdownIt from "markdown-it";
import { TypographyText } from "ant-design-vue";
const props = defineProps<{
    message: String;
    type: String;
}>();
const markdown = new MarkdownIt();
</script>
<template>
    <TypographyText v-if="props.type == 'text'">{{
        props.message
    }}</TypographyText>
    <div
        class="marking-down"
        v-else
        v-html="markdown.render(props.message)"
    ></div>
</template>
<style>
.marking-down table {
    border-collapse: collapse;
}
.marking-down td {
    border : 1px solid darkgrey;
}

</style>
