<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { storeToRefs } from "pinia";

const { variables } = storeToRefs(useWorkspaceStore());
</script>

<template>
    <div class="variable">
        <textarea v-model="variables[0]"></textarea>
    </div>
</template>

<style scoped>
.variable {
    padding: 10px;
    background-color: bisque;
    height: 100%;
}
.variable textarea {
    font-family: Arial, sans-serif; /* Replace with your desired font */
    font-size: 16px; /* Replace with your desired font size */
}
textarea {
    width: 100%;
    height: 100%;
}
</style>
