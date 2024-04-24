import { defineStore, storeToRefs } from "pinia";
import { useMessageStore } from "./messageStore";

export interface RecordBrief {
    start: string;
    duration: number;
    tokens: string;
}
export interface RecordItem{
    clientName:string;
    prompt:string;
    response:string;
    promptTokens:number;
    generationTokens:number;
    start:number;
    end:number;
    brief:RecordBrief;
}

export const useRecordStore = defineStore("record", () => {
    const ms =useMessageStore();
    const {records} = storeToRefs(ms)
    return {records}
})