import { defineStore, storeToRefs } from "pinia";
import { ref, watch } from "vue";
import { useMessageStore } from "./messageStore";
export interface MessageItem {
    content: {
        type: string;
        body: string;
    };
    sender: string;
    timeStamp: number;
}
export const useChatStore = defineStore("chat", () => {
    const messageInputting = ref("");
    const history = ref<MessageItem[]>([]);
    const ms = useMessageStore();
    const { chats } = storeToRefs(ms);
    watch(chats.value, (newValue: MessageItem[]) => {
        const newM = newValue.filter(
            (it) =>
                it.timeStamp >
                (history.value[history.value.length - 1]?.timeStamp ?? 0)
        );
        newM.forEach((it) => {
            history.value.push(it);
        });
    });
    function sendMessage() {
        const newM: MessageItem = {
            content: { body: messageInputting.value, type: "text" },
            sender: "User",
            timeStamp: Date.now(),
        };
        chats.value.push(newM);
        ms.sendMessage("/app/chats", newM);
        messageInputting.value = "";
    }

    const top = {
        content: { body: "你好", type: "text" },
        sender: "User",
        timeStamp: Date.now(),
    };
    setTimeout(() => {
        ms.sendMessage("/app/chats", top);
        chats.value.push(top);
    }, 1000);

    return {
        messageInputting,
        sendMessage,
        history,
    };
});
