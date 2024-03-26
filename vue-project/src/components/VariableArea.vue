<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { storeToRefs } from "pinia";
import { TypographyTitle, Row } from "ant-design-vue";

const { variables } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ variableIndex: number }>();
</script>

<template>
    <Row>
        <TypographyTitle :level="4">
            <span>Variables:</span>
        </TypographyTitle>
    </Row>
    <Row>
        <div class="variable">
            <textarea v-model="variables[props.variableIndex]"></textarea>
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
}
</style>
