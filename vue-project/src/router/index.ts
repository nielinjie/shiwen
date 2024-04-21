import { createRouter, createWebHistory } from "vue-router";
import MainView from "@/views/MainView.vue";

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
            component: () => import("@/views/PromptLibView.vue"),
        },
        {
            path: "/docbase",
            name: "docbase",
            component: () => import("@/views/DocbaseView.vue"),
        },
        {
            path: "/clients",
            name: "clients",
            component: () => import("@/views/ClientsView.vue"),
        },
        {
            path: "/about",
            name: "about",
            component: () => import("@/views/AboutView.vue"),
        },
    ],
});

export default router;
