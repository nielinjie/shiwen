<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { Typography,TypographyLink } from "ant-design-vue";
</script>

<template>
    <div class ="root">
        <header>
            <Typography.Title :level=1>问仓 - Ai 开发工具集</Typography.Title>
            <div class="wrapper">
                <nav>
                    <RouterLink to="/"><TypographyLink>Prompt矩阵</TypographyLink></RouterLink>
                    <RouterLink to="/clients"><TypographyLink>Api和Client</TypographyLink></RouterLink>
                    <RouterLink to="/about"><TypographyLink>关于</TypographyLink></RouterLink>
                </nav>
            </div>
        </header>
    </div>
    <RouterView />
</template>

<style scoped>
.root {
    width: 100%;
}
h1 {
    text-align: center;
}
header {
    line-height: 1.5;
    max-height: 100vh;
}



nav {
    width: 100%;
    text-align: right;
}



nav a {
    display: inline-block;
    padding: 0 1rem;
    border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
    border: 0;
}

/* @media (min-width: 1024px) {
    header {
        display: flex;
        place-items: center;
        padding-right: calc(var(--section-gap) / 2);
    }

    .logo {
        margin: 0 2rem 0 0;
    }

    header .wrapper {
        display: flex;
        place-items: flex-start;
        flex-wrap: wrap;
    }

    nav {
        text-align: left;
        margin-left: -1rem;
        font-size: 1rem;

        padding: 1rem 0;
        margin-top: 1rem;
    }
} */
</style>
