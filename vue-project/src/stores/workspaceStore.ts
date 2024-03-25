import { mande } from "mande";
import { defineStore } from "pinia";
import { ref, type Ref } from "vue";

/*
@Serializable
data class Workspace(
    val variable: String, val prompt: String, val model: String, val result: String

)
 */

export interface Task {
    client: string;
    result: string;
}
export interface WorkCell{
    x: number;
    y: number;
    tasks: Task[];
}

export const useWorkspaceStore = defineStore("workspace", () => {
    const prompts :Ref<string[]>= ref(["prompt"]);
    const variables :Ref<string[]>= ref(["variable"]);
    const cells: Ref<WorkCell[]> = ref([{x:0,y:0,tasks:[]}]);
    function save() {
        mande("/api/workspace").post({
            prompts: prompts.value,
            variables: variables.value,
            cells: cells.value,
        });
    }
    function load() {
        mande("/api/workspace")
            .get()
            .then((re) => {
                prompts.value = (re as any).prompts;
                variables.value = (re as any).variables;
                cells.value = (re as any).cells;
            });
    }
    return { prompts, variables, cells, save, load };
});
