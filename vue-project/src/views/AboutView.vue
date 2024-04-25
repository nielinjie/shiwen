<script setup lang="ts">
import {
    List,
    ListItem,
    Button,
    TypographyTitle,
    TypographyText,
} from "ant-design-vue";
import { storeToRefs } from "pinia";
import { useDocStore } from "@/stores/docStore";
import { Flex } from "ant-design-vue";
const store = useDocStore();
const { doc } = storeToRefs(store);
import MarkdownIt from "markdown-it";
const markdown = new MarkdownIt();
</script>

<template>
    <div class="r">
        <FLex gap="large" vertical>
            <TypographyTitle :level="1">关于试问：</TypographyTitle>
            <Flex gap="large">
                <TypographyText
                    ><a href="https://github.com/nielinjie/shiwen" target="_blank"
                        >https://github.com/nielinjie/shiwen</a
                    ></TypographyText
                >
                <TypographyText
                    ><a href="https://gitee.com/nielinjie127/shiwen" target="_blank"
                        >https://gitee.com/nielinjie127/shiwen</a
                    ></TypographyText
                >
            </Flex>
            <div v-html="markdown.render(doc)"></div>
        </FLex>
    </div>
</template>

<style>
.r {
    margin-left: 2em;
    margin-right: 2em;
    overflow: auto;
}
</style>
