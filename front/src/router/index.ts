import { createRouter, createWebHistory } from "vue-router";
import MainView from "@/matrix/MainView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: MainView,
        },
        {
            path: "/chat",
            name: "chat",
            component: () => import("@/views/ChatView.vue"),
        },

        {
            path: "/promptLib",
            name: "promptLib",
            component: () => import("@/promptLib/PromptLibView.vue"),
        },
        {
            path: "/docbase",
            name: "docbase",
            component: () => import("@/docbase/DocbaseView.vue"),
        },
        {
            path: "/functions",
            name: "functions",
            component: () => import("@/functions/FunctionsView.vue"),
        },
        {
            path: "/clients",
            name: "clients",
            component: () => import("@/views/ClientsView.vue"),
        },
        {
            path: "/record",
            name: "record",
            component: () => import("@/views/RecordView.vue"),
        },
        {
            path: "/about",
            name: "about",
            component: () => import("@/views/AboutView.vue"),
        },
    ],
});

export default router;
