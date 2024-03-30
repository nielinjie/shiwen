<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import { TypographyTitle, Button, Flex } from "ant-design-vue";
</script>

<template>
        <Flex vertical>
            <TypographyTitle :level="1">试问 - LLM 应用开发工具集</TypographyTitle>
            <div class="wrapper">
                <Flex justify="flex-end" gap="middle">
                    <RouterLink to="/"
                        ><Button type="link" size="large"
                            >Prompt矩阵</Button
                        ></RouterLink
                    >
                    <RouterLink to="/prompLib"
                        ><Button type="link" size="large"
                            >Prompt收藏</Button
                        ></RouterLink
                    ><RouterLink to="/docs"
                        ><Button type="link" size="large"
                            >文档知识</Button
                        ></RouterLink>   
                    <RouterLink to="/clients"
                        ><Button type="link" size="large"
                            >配置Api和模型客户端</Button
                        ></RouterLink
                    >
                    
                    <RouterLink to="/about"
                        ><Button type="link" size="large"
                            >关于</Button
                        ></RouterLink
                    >
                </Flex>
            </div>
        </Flex>
    <RouterView class="content"/>
</template>

<style scoped>
h1 {
    text-align: center;
}
header {
    line-height: 1.5;
    max-height: 100vh;
}
.content{
    margin-top: 2em;
}

</style>
