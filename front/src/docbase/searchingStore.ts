import { useDocStore } from "@/stores/docStore";
import { mande } from "mande";
import { defineStore, storeToRefs } from "pinia";
import { ref } from "vue";
import { useDocbaseStore, type DocInfo } from "./docbaseStore";
export interface Search{
    indexer: string;
    queryString:string;
}
export const useSearchingStore = defineStore("searching", () => {
    const searchInputting  = ref("");
    const searchMethods = ref([""]);
    const searchMethodSelected = ref("");
    const {derivedDocs,condition} = storeToRefs(useDocbaseStore())
    mande("/api/docbase/indexers").get().then((r)=>{
        console.log(r as string[]);
        searchMethods.value = r as string[];
    })
    function search(){
        mande("/api/docbase/search")
            .post({
                indexer: searchMethodSelected.value,
                queryString: searchInputting.value,
            })
            .then((r) => {
                derivedDocs.value = (r as DocInfo[])
                condition.value = `搜索结果 - ${searchInputting.value}(by: ${searchMethodSelected.value})`;
            });
    }
    return {
        searchInputting,
        searchMethods,
        searchMethodSelected,
        search
    }
})