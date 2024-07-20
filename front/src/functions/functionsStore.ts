import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";
export interface Fun{
    name:string,description:string
}
export const useFunctionsStore = defineStore("functions", () => {
    const functions = ref<Fun[]>([]);
    mande("/api/functions")
        .get()
        .then((r) => {
            functions.value = r as Fun[];
        });
    return { functions };
});
