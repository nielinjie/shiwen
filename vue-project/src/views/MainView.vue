<script setup lang="ts">
import VariableArea from "../components/VariableArea.vue";
import PromptArea from "../components/PromptArea.vue";
import RunningArea from "../components/RunningArea.vue";
import WorkshopArea from "@/components/WorkshopArea.vue";
import { Row, Col } from "ant-design-vue";
import { useWorkspaceStore } from "@/stores/workspaceStore";

const workspaceStore = useWorkspaceStore();
</script>

<template>
    <table>
        <tr>
            <td>
                <div>
                    <WorkshopArea></WorkshopArea>
                </div>
            </td>
            <td v-for="(prompt, index) in workspaceStore.prompts" :key="index">
                <div class="area">
                    <PromptArea :promptIndex="index"></PromptArea>
                </div>
            </td>
        </tr>
        <tr
            v-for="(v, vIndex) in workspaceStore.variables"
            :key="vIndex"
            :class="vIndex % 2 === 0 ? 'highlight' : ''"
        >
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
</template>

<style scoped>
div {
    padding: 2px;
}
.highlight {
    /* background-color: rgba(179, 223, 247, 0.25); */
}
td {
    border-right: solid 1px rgba(179, 223, 247, 01);
    border-bottom: solid 1px rgba(179, 223, 247, 01);
}
</style>
