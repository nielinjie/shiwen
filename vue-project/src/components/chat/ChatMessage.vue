<script setup lang="ts">
import { defineProps } from "vue";
import MarkdownIt from "markdown-it";
import { TypographyText } from "ant-design-vue";
const props = defineProps<{
    message: {body:string,type:string};
}>();
const markdown = new MarkdownIt();
</script>
<template>
    <TypographyText v-if="props.message.type == 'text'">{{
        props.message.body
    }}</TypographyText>
    <div
        class="marking-down"
        v-else
        v-html="markdown.render(props.message.body)"
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
