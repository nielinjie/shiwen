<script setup lang="ts">
import VariableArea from "@/components/matrix/VariableArea.vue";
import PromptArea from "@/components/matrix/PromptArea.vue";
import RunningArea from "@/components/matrix/RunningArea.vue";
import WorkshopArea from "@/components/WorkshopArea.vue";
import { useWorkspaceStore } from "@/stores/workspaceStore";

const workspaceStore = useWorkspaceStore();
</script>

<template>
    <div class="content">
        <table>
            <tr>
                <td>
                    <div>
                        <WorkshopArea></WorkshopArea>
                    </div>
                </td>
                <td
                    v-for="(prompt, index) in workspaceStore.prompts"
                    :key="index"
                >
                    <div class="area">
                        <PromptArea :promptIndex="index"></PromptArea>
                    </div>
                </td>
            </tr>
            <tr v-for="(v, vIndex) in workspaceStore.variables" :key="vIndex">
                <td>
                    <div class="area">
                        <VariableArea :variableIndex="vIndex"></VariableArea>
                    </div>
                </td>
                <td
                    v-for="(prompt, pIndex) in workspaceStore.prompts"
                    :key="pIndex"
                >
                    <div class="area">
                        <RunningArea :x="pIndex" :y="vIndex"></RunningArea>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</template>

<style scoped>
div {
    padding: 4px;
}

td {
    border-right: solid 1px rgba(179, 223, 247, 01);
    border-bottom: solid 1px rgba(179, 223, 247, 01);
}
.content {
    margin-left: 2em;
    margin-right: 2em;
    overflow: auto;
}
</style>
