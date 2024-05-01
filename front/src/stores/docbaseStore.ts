import { mande } from "mande";
import { defineStore } from "pinia";
import { ref, type Ref } from "vue";

export interface DocInfo {
    id: string;
    name: string;
}
export interface DocObject extends DocInfo {
    content: string;
    attrs: Record<string, string>;
}

export const useDocbaseStore = defineStore("docbase", () => {
    const baseInfo = ref("");
    const docs: Ref<DocInfo[]> = ref([]);
    const selectedDocInfo: Ref<DocInfo | null> = ref(null);
    const docDetail: Ref<DocObject | null> = ref(null);
    const searching = ref("");

    mande("/api/docbase/info")
        .get()
        .then((re) => (baseInfo.value = (re as any).info as string));
    mande("/api/docbase/docs")
        .get()
        .then((re) => {
            docs.value = re as DocInfo[];
        });
    function selectDoc(doc: DocInfo) {
        selectedDocInfo.value = doc;
        mande("/api/docbase/docs/" + btoa(unescape(encodeURIComponent(doc.id))))
            .get()
            .then((re) => {
                docDetail.value = re as DocObject;
            });
    }
    function searchDoc() {}
    const searchMethods = ref([
        { value: "embedding", text: "Embedding" },
        { value: "keywords", text: "Keywords" },
    ]);
    const searchMethod = ref("embedding");
    return {
        baseInfo,
        docs,
        selectedDocInfo,
        selectDoc,
        docDetail,
        searching,
        searchDoc,
        searchMethod,
        searchMethods,
    };
});
