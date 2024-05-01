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
    const referringDocs: Ref<DocInfo[]> = ref([]);
    const selectedRefDocInfo: Ref<DocInfo | null> = ref(null);
    const derivedDocs: Ref<DocInfo[]> = ref([]);
    const docDetail: Ref<DocObject | null> = ref(null);
    const condition = ref("");

    mande("/api/docbase/info")
        .get()
        .then((re) => (baseInfo.value = (re as any).info as string));
    mande("/api/docbase/referringDocs")
        .get()
        .then((re) => {
            referringDocs.value = re as DocInfo[];
        });
    function selectRefDoc(doc: DocInfo) {
        selectedRefDocInfo.value = doc;
        condition.value = `来自原始文件 - ${doc.id}`
        mande(
            "/api/docbase/derivedDocs/referring/" +
                btoa(unescape(encodeURIComponent(doc.id)))
        )
            .get()
            .then((re) => {
                derivedDocs.value = re as DocInfo[];
            });
        mande("/api/docbase/docs/" + btoa(unescape(encodeURIComponent(doc.id))))
            .get()
            .then((re) => {
                docDetail.value = re as DocObject;
            });
    }
    function selectDerDoc(doc: DocInfo) {
        mande("/api/docbase/docs/" + btoa(unescape(encodeURIComponent(doc.id))))
            .get()
            .then((re) => {
                docDetail.value = re as DocObject;
            });
    }
    
    return {
        baseInfo,
        referringDocs,
        condition,
        selectRefDoc,
        selectDerDoc,
        derivedDocs,
        docDetail,
    };
});
