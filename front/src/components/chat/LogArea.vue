<script setup lang="ts">
import { ref } from "vue";
import { storeToRefs } from "pinia";
import { List, Flex, TypographyTitle, ListItem } from "ant-design-vue";

import { useLogStore } from "@/stores/logStore";
import { TypographyText } from "ant-design-vue";

export interface MessageItemDisplay {
    message: string;
    level: "info" | "error" | "warn";
}

const logStore = useLogStore();
const { logs } = storeToRefs(logStore);

const chatList = ref<any>(null);

</script>
<template>
    <div>
        <Flex vertical gap="large">
            <TypographyTitle :level="5">Log Here: </TypographyTitle>
            <div class="chatList" ref="chatList">
                <List size="large" bordered :dataSource="logs">
                    <template #renderItem="{ item }">
                        <ListItem>
                            <TypographyText>{{ item.message }}</TypographyText>
                        </ListItem>
                    </template>
                </List>
            </div>
        </Flex>
    </div>
</template>
<style scoped>
.chatList {
    width: 20vw;
    height: 50vh;
    overflow: scroll;
}</style>
