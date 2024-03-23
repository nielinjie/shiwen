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
    runner:string,result:string
}

export const useWorkspaceStore = defineStore("workspace", () => {
    const prompt = ref("prompt");
    const variable = ref("variable");
    const tasks:Ref<Task[]> = ref([]);
    function save() {
        mande("/api/workspace").post({
            prompt: prompt.value,
            variable: variable.value,
            tasks: tasks.value,
        });
    }
    function load(){
        mande("/api/workspace").get().then((re) => {
            prompt.value = (re as any).prompt;
            variable.value = (re as any).variable;
            tasks.value = (re as any).tasks;
        })
    }
    return { prompt, variable, tasks ,save,load};
});
