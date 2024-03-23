<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useWorkspaceStore } from '@/stores/workspaceStore';

const { prompt } = storeToRefs(useWorkspaceStore());
</script>
<template>
    <div class="variable">
        <textarea v-model="prompt"></textarea>
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
