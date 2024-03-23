import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useRunnerStore = defineStore("runner", () => {
    const runners = ref([""]);
    mande("/api/runners")
        .get()
        .then((re) => {
            runners.value = re as string[];
        });
    return { runners };
});
