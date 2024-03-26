<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { TypographyTitle, Row, Flex, Button } from "ant-design-vue";

const { prompts } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ promptIndex: number }>();
const workspaceStore = useWorkspaceStore();

</script>
<template>
    <Flex :vertical="true" gap="middle">
        <Flex align="end" gap="large">
            <TypographyTitle :level="5">
                {{ props.promptIndex === 0 ? "Prompts:" : "&nbsp;" }}
            </TypographyTitle>
            <Button
                v-if="props.promptIndex === 0"
                @click="workspaceStore.addPrompt"
            >
                +</Button
            >
            <Button  @click="workspaceStore.removePrompt(promptIndex)"> -</Button>
        </Flex>

        <div class="variable">
            <textarea v-model="prompts[props.promptIndex]"></textarea>
        </div>
    </Flex>
</template>

<style scoped>
.variable {
    padding: 1em;
    background-color: bisque;
    /* height: 100%; */
}
.variable textarea {
    font-family: Arial, sans-serif;
    font-size: 16px;
    border-radius: 5px;
    padding: 1em;
    border-color: darkcyan;
    height: 20vh;
}
</style>
