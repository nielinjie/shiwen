import { defineStore } from "pinia";
import { computed, ref, watch } from "vue";
import { useSocketStore, type Message } from "./socketStore";
export interface MessageItemDisplay {
    message: {
        type: string;
        body: string;
    };
    sender: string;
}
export const useChatStore = defineStore("chat", () => {
    const messageInputting = ref("");
    const socketStore = useSocketStore();
    const history = ref<Message[]>([]);
    watch(
        socketStore.socketMessages, 
        (newMessages) => {
        const newM = newMessages.filter(
            (it) =>
                it.timestamp >
                (history.value[history.value.length - 1]?.timestamp ?? 0)
        );
        newM.forEach((it) => {
            history.value.push(it)
        })
        
    });
    function sendMessage() {
        socketStore.sendMessage({
            content: {
                body:messageInputting.value,
                type:"text"
            },
            direct: "up",
            timestamp: Date.now(),
        });
        messageInputting.value = "";
    }
    setTimeout(() => {
        socketStore.openSocket();
    }, 100);
    const top = {
        message: { body: "Welcome to the chat!", type: "text" },
        sender: "Bot",
    };

    const messagesHistory = ref<MessageItemDisplay[]>([top]);
    watch(history.value, (newVal) => {
        messagesHistory.value = [
            top,
            ...newVal.map((message: Message) => ({
                message: message.content,
                sender: message.direct === "down" ? "Bot" : "User",
            })),
        ];
    });
    return {
        messageInputting,
        sendMessage,
        history,
        messagesHistory,
    };
});
