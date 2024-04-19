import { defineStore } from "pinia";
import { computed, ref, watch } from "vue";
import { useSocketStore, type Message } from "./socketStore";

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
    return {
        messageInputting,
        sendMessage,
        history,
    };
});
