package cloud.qingyangyunyun.ai.promptLib

import cloud.qingyangyunyun.ai.workshop.Paths
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Serializable
data class PromptRequest(
    val content: String,
    val title: String,
    val sourceId: String,
    val meta: Map<String, String> = mapOf()
)

@Component
class SeedService(@Autowired val paths: Paths, @Autowired val localStorageSeed: LocalStorageSource) {
    private val seeds = mutableMapOf<String, Source>()

    init {
        seeds["local-storage"] = localStorageSeed
    }


    fun search(q: String): List<Prompt> {
        if(q.isEmpty()){
            return seeds.values.flatMap {
                it.prompts()
            }
        }
        return seeds.values.flatMap {
            it.prompts().filter {
                it.title.contains(q)
                        || it.content.contains(q)
            }
        }
    }


    fun getSeeds(): List<Source> {
        return seeds.values.toList()
    }

    fun savePrompt(prompt: PromptRequest) {
        require(prompt.sourceId == "local-storage") { "only local-storage supported" }
        localStorageSeed.savePrompt(prompt)
    }
}

@Component
class PromptService(@Autowired val paths: Paths) {
    private val prompts = mutableMapOf<String, Prompt>()

    fun getPrompt(id: String): Prompt? {
        return prompts[id]
    }

    fun savePrompt(prompt: Prompt) {
        prompts[prompt.id] = prompt
    }
}