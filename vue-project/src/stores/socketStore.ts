import { defineStore } from "pinia";
import { ref } from "vue";

export interface Message {
    content: any;
    direct: "up" | "down";
    timestamp: number;
}
type SocketState = "open" | "closed" | "error";

export const useSocketStore = defineStore("socket", () => {
    const protocol = location.protocol === "https:" ? "wss:" : "ws:";
    const wsUrl = `${protocol}//${location.host}/api/ws`;
    const socket = ref<WebSocket | null>(null);
    const socketUrl = ref(wsUrl);
    const socketStatus = ref<SocketState>("closed");
    const socketMessages = ref<Message[]>([]);
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
            console.log(e)
            console.log(e.data)
            socketMessages.value.push({
                content: e.data,
                direct: "down",
                timestamp: Date.now(),
            });
        };
    }
    function closeSocket() {
        if (socket.value) {
            socket.value.close();
        }
    }
    function sendMessage(message: Message) {
        if (socket.value) {
            socket.value.send(JSON.stringify(message));
            socketMessages.value.push(message);
        }
    }
    return {
        socket,
        socketUrl,
        socketStatus,
        socketMessages,
        openSocket,
        closeSocket,
        sendMessage,
    };
});
