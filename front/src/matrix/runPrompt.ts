import { defineStore, storeToRefs } from "pinia";
import { mande } from "mande";
import { useWorkspaceStore, type Task } from "../stores/workspaceStore";
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

    async function runIt(pIndex:number,vIndex:number,runningIndex: number, client: string) {
        const request = {
            input: variables.value[vIndex],
            prompt: prompts.value[pIndex],
            client: client,
        };

        setResult(pIndex, vIndex, runningIndex, "running...");
        runApi.post(request).then((re) => {
            setResult(pIndex, vIndex, runningIndex, (re as Task).result);
        });
    }

    return { runIt };
});
