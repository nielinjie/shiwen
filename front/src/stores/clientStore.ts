import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useClientStore = defineStore("client", () => {
    const clients = ref([""]);
    const configs = ref("");

    mande("/api/clients")
        .get()
        .then((re) => {
            clients.value = re as string[];
        });
    mande("/api/configs")
        .get()
        .then(re => configs.value = JSON.stringify( re,null,2));
    function saveConfigs() {
        mande("/api/configs").post({ string: configs.value });
    }
    function loadConfigs(){
        mande("/api/configs")
            .get()
            .then(re => configs.value = JSON.stringify( re,null,2));
    }
    function loadExample(){
        mande("/api/configs/example")
            .get()
            .then(re => configs.value = JSON.stringify( re,null,2));
    }
    return { clients, configs, saveConfigs ,loadConfigs,loadExample};
});
