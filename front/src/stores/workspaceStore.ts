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
export interface WorkCell {
    x: number;
    y: number;
    tasks: Task[];
}

export const useWorkspaceStore = defineStore("workspace", () => {
    const prompts: Ref<string[]> = ref([""]);
    const variables: Ref<string[]> = ref([""]);
    const cells: Ref<WorkCell[]> = ref([{ x: 0, y: 0, tasks: [] }]);
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
    function addPrompt(text:string="") {
        prompts.value.push(text);
        variables.value.forEach((variable, index) => {
            cells.value.push({
                y: index,
                x: prompts.value.length - 1,
                tasks: [],
            });
        });
    }
    function removePrompt(index: number) {
        prompts.value.splice(index, 1);
        cells.value = cells.value.filter((cell) => cell.x !== index);
        replaceCellAfterX(index);
    }
    function replaceCellAfterX(xIndex:number){
        cells.value.filter(cell => cell.x > xIndex).forEach(cell => cell.x = cell.x - 1);
    }
    function addVariable() {
        variables.value.push("");
        prompts.value.forEach((prompt, index) => {
            cells.value.push({
                y: variables.value.length - 1,
                x: index,
                tasks: [],
            });
        });
    }
    function removeVariable(index: number) {
        variables.value.splice(index, 1);
        cells.value = cells.value.filter((cell) => cell.y !== index);
        replaceCellAfterY(index);
    }
    function replaceCellAfterY(yIndex:number){
        cells.value.filter(cell => cell.y > yIndex).forEach(cell => cell.y = cell.y - 1);
    }
    return {
        prompts,
        variables,
        cells,
        save,
        load,
        addPrompt,
        removePrompt,
        addVariable,
        removeVariable,
    };
});
