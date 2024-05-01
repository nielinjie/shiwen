import { mande } from "mande";
import { defineStore } from "pinia";
import { computed, ref, type Ref } from "vue";

export interface SourceInfo {
    name: string;
    description: string;
    id: string;
    type: string;
}
interface SourceAndState extends SourceInfo {
    checked: boolean;
}
export interface Prompt {
    content: string;
    title: string;
    id: string;
    sourceId: string;
}

export const usePromptLibStore = defineStore("promptLib", () => {
    const sources: Ref<SourceAndState[]> = ref([]);
    const sourcesIdToName = computed(() => {
        const result: Record<string, string> = {};
        sources.value.forEach((s) => {
            result[s.id] = s.name;
        });
        return result;
    });
    const storages = computed(() =>
        sources.value.filter((s) => s.type == "storage")
    );
    const seeds = computed(() => sources.value.filter((s) => s.type == "seed"));
    const prompts: Ref<Prompt[]> = ref([]);
    const query = ref("");
    function loadSources() {
        mande("/api/sources")
            .get()
            .then((re) => {
                sources.value = re as SourceAndState[];
            });
    }

    function searchPrompt() {
        mande("/api/prompts")
            .get({ query: { q: query.value } })
            .then((re) => {
                prompts.value = re as Prompt[];
            });
    }
    loadSources();
    searchPrompt();
    function savePrompt(content: string, title: string, sourceId: string) {
        mande("/api/prompts")
            .post({ content, title, sourceId })
            .then((re) => {
                searchPrompt();
            });
    }
    return {
        sources,
        storages,
        seeds,
        sourcesIdToName,
        prompts,
        query,
        savePrompt,
        searchPrompt,
        loadSources,
    };
});
