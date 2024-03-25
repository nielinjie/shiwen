import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useClientStore = defineStore("client", () => {
    const clients = ref([""]);
    mande("/api/clients")
        .get()
        .then((re) => {
            clients.value = re as string[];
        });
    return { clients };
});
