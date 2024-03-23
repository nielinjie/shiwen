import { ref, computed } from "vue";
import { defineStore, storeToRefs } from "pinia";
import { mande } from "mande";
import { useWorkspaceStore, type Task } from "./workspaceStore";
import { replaceAt } from "@/util";
const runApi = mande("/api/run");



export const useRunPromptStore = defineStore("runPrompt", () => {
    const { prompt, variable, tasks } = storeToRefs(useWorkspaceStore());
    async function runIt(runningIndex: number, runner: string) {
        const request = {
            input: variable.value,
            prompt: prompt.value,
            runner: runner,
        };

        tasks.value = replaceAt(tasks.value, runningIndex, (task: Task) => ({
            ...task,
            result: "running...",
        }));
        runApi.post(request).then((re) => {
            tasks.value = replaceAt(
                tasks.value,
                runningIndex,
                (task:Task) => re as Task
            );
        });
    }

    return { runIt };
});
