<script setup lang="ts">
import VariableArea from "../components/VariableArea.vue";
import PromptArea from "../components/PromptArea.vue";
import RunningArea from "../components/RunningArea.vue";
import WorkshopArea from "@/components/WorkshopArea.vue";
import { Row, Col } from "ant-design-vue";
</script>

<template>
    <Row style="height: 200px">
        <Col :span="6">
            <div>
                <WorkshopArea></WorkshopArea>
            </div>
        </Col>
        <Col :span="10">
            <div class="area">
                <PromptArea></PromptArea>
            </div>
        </Col>
    </Row>
    <Row style="height: 400px">
        <Col :span="6">
            <div class="area">
                <VariableArea></VariableArea>
            </div>
        </Col>
        <Col :span="10">
            <div class="area">
                <RunningArea :x="0" :y="0"></RunningArea>
            </div>
        </Col>
    </Row>
</template>

<style scoped>
div {
    padding: 10px;
}
.area {
    height: 100%;
}

.body {
    height: 300px;
}
</style>
