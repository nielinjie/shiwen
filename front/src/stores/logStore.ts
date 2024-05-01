import { defineStore, storeToRefs } from "pinia";
import { useMessageStore } from "./messageStore";

export interface LogMessage {
    id: String;
    level: "info" | "error" | "warn";
    message: string;
    timestamp: number;
}
type SocketState = "open" | "closed" | "error";

export const useLogStore = defineStore("log", () => {
    const { logs } = storeToRefs(useMessageStore());
    return { logs };
});
