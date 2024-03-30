
import { createApp  } from "vue";
import { createPinia } from "pinia";
import Antd from "ant-design-vue";

import App from "./App.vue";
import router from "./router";


const app = createApp(App);
app.directive("visibility", {
    mounted(el, binding, vnode) {
        el.style.visibility = binding.value ? "visible" : "hidden";
    },
    updated(el, binding, vnode) {
        el.style.visibility = binding.value ? "visible" : "hidden";
    },
});
app.use(createPinia());
app.use(router);
app.use(Antd);
app.mount("#app");
