<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from "vue-router";
import {
    TypographyText,
    TypographyTitle,
    Layout,
    LayoutContent,
    LayoutHeader,
    Menu,
    MenuItem,
    LayoutFooter,
} from "ant-design-vue";
import { ref } from "vue";
const selectedMenu = ref<string[]>(["/"]);
const router = useRouter();

router.afterEach((to) => {
    selectedMenu.value = [to.path];
});
</script>

<template>
    <Layout>
        <LayoutHeader>
            <Menu
                mode="horizontal"
                v-model:selectedKeys="selectedMenu"
                theme="dark"
                style="float: left"
            >
                <MenuItem key="/">
                    <RouterLink to="/"> Prompt矩阵 </RouterLink>
                </MenuItem>
                <MenuItem key="/chat">
                    <RouterLink to="/chat">对话调校</RouterLink>
                </MenuItem>
                <MenuItem key="/promptLib">
                    <RouterLink to="/promptLib">Prompt收藏</RouterLink></MenuItem
                >
                <MenuItem key="/docbase">
                    <RouterLink to="/docbase"> 文档知识管理</RouterLink>
                </MenuItem>
                <MenuItem key="/functions">
                    <RouterLink to="/functions">FunctionCalling</RouterLink>
                </MenuItem>
                <MenuItem key="/clients">
                    <RouterLink to="/clients">配置Api和模型客户端</RouterLink>
                </MenuItem>
                <MenuItem key="/record">
                    <RouterLink to="/record">用量</RouterLink>
                </MenuItem>
                <MenuItem key="/about">
                    <RouterLink to="/about">关于</RouterLink>
                </MenuItem>
            </Menu>
            <TypographyTitle
                :level="2"
                style="float: right; color: lightgray; margin-top: 1rem"
                >试问</TypographyTitle
            >
        </LayoutHeader>
        <LayoutContent style="padding-top: 1em; min-height: 85vh">
            <RouterView class="content" />
        </LayoutContent>
        <LayoutFooter></LayoutFooter>
    </Layout>
</template>

<style scoped></style>
