<script setup lang="ts">
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { useRunPromptStore } from "../stores/runPrompt";
import { storeToRefs } from "pinia";
import { TypographyTitle, Row, Flex ,Button} from "ant-design-vue";

const { variables } = storeToRefs(useWorkspaceStore());
const props = defineProps<{ variableIndex: number }>();
const workspaceStore = useWorkspaceStore();
</script>

<template>
    <Flex :vertical="true" gap="middle">
        <Flex align="end" gap="large">
            <TypographyTitle :level="5">
                {{ props.variableIndex === 0 ? "Variables:" : "&nbsp;" }}
            </TypographyTitle>
<Button
                v-if="props.variableIndex === 0"
                @click="workspaceStore.addVariable"
            >
                +</Button
            >
            <Button  @click="workspaceStore.removeVariable(variableIndex)"> -</Button>        </Flex>
        <div class="variable">
            <textarea v-model="variables[props.variableIndex]"></textarea>
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
}
</style>
