<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { TypographyTitle, Row } from "ant-design-vue";

const { prompts } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ promptIndex: number }>();
</script>
<template>
    <Row>
        <TypographyTitle :level="4">
            <span>Prompt:</span>
        </TypographyTitle>
    </Row>
    <Row>
        <div class="variable">
            <textarea v-model="prompts[props.promptIndex]"></textarea>
        </div>
    </Row>
</template>

<style scoped>
.variable {
    padding: 1em;
    background-color: bisque;
    /* height: 100%; */
}
.variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 1em;
    border-color: darkcyan;
    height: 20vh;
}

</style>
