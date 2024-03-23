<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { storeToRefs } from "pinia";

const { variable } = storeToRefs(useWorkspaceStore());
</script>

<template>
    <div class="variable">
        <textarea v-model="variable"></textarea>
    </div>
</template>

<style scoped>
.variable {
    padding: 10px;
    background-color: bisque;
    height: 100%;
}
textarea {
    width: 100%;
    height: 100%;
}
</style>
