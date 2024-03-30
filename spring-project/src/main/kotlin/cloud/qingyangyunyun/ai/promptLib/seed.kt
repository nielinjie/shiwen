package cloud.qingyangyunyun.ai.promptLib


interface Seed {
    fun prompts(): List<Prompt>
}

data class Prompt(val content: String, val meta: Map<String, String>) {

}