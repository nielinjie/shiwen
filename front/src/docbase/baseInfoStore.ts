import { mande } from "mande";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useBaseInfoStore = defineStore("baseInfo", () => {
    const manualIndexers = ref<string[]>([]);
    const manualIndexerSelected = ref<string>("");
    mande("/api/docbase/indexers/manual")
        .get()
        .then((res) => {
            manualIndexers.value = res as string[];
            manualIndexerSelected.value = manualIndexers.value[0];
        });
    const fileLocationList = ref<string[]>([]);
    mande("/api/docbase/config/files").get().then((res) => {
        fileLocationList.value = res as string[];
    });
    function index() {
        mande("/api/docbase/manualIndex/" + manualIndexerSelected.value)
            .post()
            .then((res) => {});
    }
    function saveFileLocationList() {
        mande("/api/docbase/config/files")
            .post(fileLocationList.value)
            .then((res) => {});
    }
    return { manualIndexerSelected, manualIndexers, fileLocationList ,saveFileLocationList,index};
});
