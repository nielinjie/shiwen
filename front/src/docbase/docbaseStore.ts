import { mande } from "mande";
import { defineStore } from "pinia";
import { ref, type Ref } from "vue";

export interface DocInfo {
    id: string;
    name: string;
    meta: Map<string, string>;
}
export interface DocObject extends DocInfo {
    content: string;
    attrs: Record<string, string>;
}
export interface IndexerStatus {
    id: string;
    indexerNames: string[];
}

export const useDocbaseStore = defineStore("docbase", () => {
    const baseInfo = ref("");
    const referringDocs: Ref<DocInfo[]> = ref([]);
    const selectedRefDocInfo: Ref<DocInfo | null> = ref(null);
    const derivedDocs: Ref<DocInfo[]> = ref([]);
    const docDetails: Ref<DocObject[] > = ref([]);
    const docDetail: Ref<DocObject | null> = ref(null);

    const condition = ref("");

    mande("/api/docbase/info")
        .get()
        .then((re) => (baseInfo.value = JSON.stringify((re as any).info,null,2) ));
    mande("/api/docbase/referringDocs")
        .get()
        .then((re) => {
            referringDocs.value = re as DocInfo[];
        });
    function selectRefDoc(doc: DocInfo) {
        selectedRefDocInfo.value = doc;
        condition.value = `来自原始文件 - ${doc.id}`;
        mande(
            "/api/docbase/derivedDocs/referring/" +
                btoa(unescape(encodeURIComponent(doc.id)))
        )
            .get()
            .then((re) => {
                derivedDocs.value = re as DocInfo[];
                updateDetails()
            });
        
    }
    function selectDerDoc(doc: DocInfo) {
        mande("/api/docbase/docs/" + btoa(unescape(encodeURIComponent(doc.id))))
            .get()
            .then((re) => {
                docDetail.value = re as DocObject;
            });
    }
    function updateDetails(){
        mande("/api/docbase/docs/ids")
            .post(derivedDocs.value.map((d) => d.id))
            .then((re) => {
                console.log(re);
                docDetails.value = re as DocObject[];
                console.log(docDetails.value);
            });
    }

    return {
        baseInfo,
        referringDocs,
        condition,
        selectRefDoc,
        selectDerDoc,
        derivedDocs,
        docDetails,
        docDetail,
        updateDetails
    };
});
