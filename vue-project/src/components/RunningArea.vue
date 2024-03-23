<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { Button, Space, Row, Col } from "ant-design-vue";
import { storeToRefs } from "pinia";
import { useRunnerStore } from "@/stores/runnerStore";

const rpStore = useRunPromptStore();
const { tasks } = storeToRefs(useWorkspaceStore());
const {runners} = storeToRefs(useRunnerStore())
function addTask() {
    tasks.value.push({
        result: "",
        runner: runners.value[0]
    });
}
</script>
<template>
        <Row>
            <Button @click="addTask">
                <span>Add</span>
            </Button>
        </Row>
        <Row class="main">
            <Col v-for="(task, index) in tasks" :span="12">
                <RunningItem :index="index"></RunningItem>
            </Col>
        </Row>
</template>
<style scoped>
.main {
    height: 100%;
}
</style>
