import { ref, computed } from "vue";
import { defineStore, storeToRefs } from "pinia";
import { mande } from "mande";
import { useWorkspaceStore, type Task } from "./workspaceStore";
import { replaceAt } from "@/util";
const runApi = mande("/api/run");

export const useRunPromptStore = defineStore("runPrompt", () => {
    const { prompts, variables, cells } = storeToRefs(useWorkspaceStore());
    function setResult(x: number, y: number, index: number, result: string) {
        const tasks = cells.value.find((c) => c.x === x && c.y === y)?.tasks;
        if (tasks) {
            tasks[index].result = result;
        }
    }

    async function runIt(runningIndex: number, client: string) {
        const request = {
            input: variables.value[0],
            prompt: prompts.value[0],
            client: client,
        };

        setResult(0, 0, runningIndex, "running...");
        runApi.post(request).then((re) => {
            setResult(0, 0, runningIndex, (re as Task).result);
        });
    }

    return { runIt };
});
