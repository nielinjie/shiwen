import { defineStore } from "pinia";
import { ref } from "vue";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client/dist/sockjs.min.js";
import type { LogMessage } from "./logStore";
import type { MessageItem } from "./chatStore";
import type { RecordItem } from "./recordStore";


export const useMessageStore = defineStore("message", () => {
    const messages = ref<any[]>([]);
    const logs = ref<LogMessage[]>([]);
    const chats = ref<MessageItem[]>([]);
    const records = ref<RecordItem[]>([]);
    // const stompClient = Stomp.over(new SockJs("/api/stomp"));
    const stompClient = new Client({
        webSocketFactory: () => {
            return new SockJS("/api/stomp");
        },
        onConnect: () => {
            stompClient.subscribe("/topic/greetings", (msg: any) => {
                messages.value.push(JSON.parse(msg.body));
            });
            stompClient.subscribe("/topic/logs", (msg: any) => {
                logs.value.push(JSON.parse(msg.body));
            });
            stompClient.subscribe("/user/queue/chats", (msg: any) => {
                chats.value.push({
                    ...JSON.parse(msg.body),
                    sender: "Bot",timeStamp: Date.now(),
                });
            });
            stompClient.subscribe("/topic/records", (msg: any) => {
                records.value.push(JSON.parse(msg.body));
            });
        },
    });

    setTimeout(() => {
        stompClient.activate();
    }, 100);

    function sendMessage(destination: string, body: object) {
        stompClient.publish({ destination, body: JSON.stringify(body) });
    }

    return { messages, sendMessage, logs,chats,records };
});
