import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useDocStore = defineStore("doc", () => {
    const doc = ref("");
    mande("/api/markdown/README.MD")
        .get()
        .then((re) => {
            doc.value = (re as any).content;
        });
    return { doc };
});
