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
class SourceService(
    @Autowired val paths: Paths,
    @Autowired val localStorageSeed: LocalStorageSource,
    @Autowired val promptsChatSeed: PromptsChatSeed
) {
    private val sources = mutableMapOf<String, Source>()

    init {
        sources["local-storage"] = localStorageSeed
        sources["https://prompts.chat"] = promptsChatSeed
    }


    fun search(q: String): List<Prompt> { //TODO change to embedding+vector searching
        if (q.isEmpty()) {
            return sources.values.flatMap {
                it.prompts()
            }
        }
        return sources.values.flatMap {
            it.prompts().filter {
                it.title.contains(q)
                        || it.content.contains(q)
            }
        }
    }


    fun getSources(): List<Source> {
        return sources.values.toList()
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