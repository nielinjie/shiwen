<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useRunPromptStore } from "../stores/runPrompt";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { Button, Select, SelectOption } from "ant-design-vue";
import { ref, watch } from "vue";
import { useRunnerStore } from "@/stores/runnerStore";
import { replaceAt } from "@/util";
const rpStore = useRunPromptStore();
const workspaceStore = useWorkspaceStore();
const props = defineProps(["index"]);
const { tasks } = storeToRefs(workspaceStore);
const { runners } = storeToRefs(useRunnerStore());
function removeMe() {
    tasks.value.splice(props.index, 1);
}
const runner = ref(tasks.value[props.index].runner);

watch(runner, () => {
    tasks.value = replaceAt(tasks.value, props.index,(task) =>({
        ...task,
        runner: runner.value,
    }));
});
watch(tasks,()=>{
    runner.value = tasks.value[props.index].runner;
})
</script>

<template>
    <div class="variable">
        <Select v-model:value="runner">
            <SelectOption
                v-for="runner in runners"
                :key="runner"
                :value="runner"
                >{{ runner }}</SelectOption
            >
        </Select>
        <Button @click="rpStore.runIt(index, runner)">
            <span>Run</span>
        </Button>
        <Button @click="removeMe">-</Button>
        <textarea v-model="tasks[index].result"></textarea>
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
    height: 80%;
}
</style>
