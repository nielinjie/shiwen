import { defineStore } from "pinia";
import { ref } from "vue";
import type { Message } from "./socketStore";

export interface LogMessage {
    id:String
    level: "info" | "error" | "warn";
    message: string;
    timestamp: number;
}
type SocketState = "open" | "closed" | "error";

export const useLogStore = defineStore("log", () => {
    const protocol = location.protocol === "https:" ? "wss:" : "ws:";
    const wsUrl = `${protocol}//${location.host}/api/logws`;
    const socket = ref<WebSocket | null>(null);
    const socketUrl = ref(wsUrl);
    const socketStatus = ref<SocketState>("closed");
    const socketMessages = ref<LogMessage[]>([]);
    function openSocket() {
        if (socket.value) {
            socket.value.close();
        }
        socket.value = new WebSocket(socketUrl.value);
        socket.value.onopen = () => {
            socketStatus.value = "open";
        };
        socket.value.onclose = () => {
            socketStatus.value = "closed";
        };
        socket.value.onmessage = (e) => {
                            console.log(e.data);

            socketMessages.value.push(
                JSON.parse(e.data) as LogMessage
            );
        };
    }
    function closeSocket() {
        if (socket.value) {
            socket.value.close();
        }
    }
   setTimeout(() => {
    openSocket();
   }, 200);
    return {
        socket,
        socketUrl,
        socketStatus,
        socketMessages,
        openSocket,
        closeSocket,
    };
});
