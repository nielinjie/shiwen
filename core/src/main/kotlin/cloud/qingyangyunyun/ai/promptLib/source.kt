package cloud.qingyangyunyun.ai.promptLib

import cloud.qingyangyunyun.ai.JsonData
import cloud.qingyangyunyun.ai.Paths
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.common.base.id

interface Source {
    fun prompts(): List<Prompt>
    fun info(): SourceInfo
}

@Serializable
data class SourceInfo(
    val name: String, val description: String, val id: Id, val type: String
)

@Serializable
data class Prompt(
    val content: String, val title: String, val id: Id, val sourceId: Id, val meta: Map<String, String>
)

@Component
class LocalStorageSource(@Autowired val paths: Paths) : Source {
    val prompts = mutableListOf<Prompt>()
    val data = JsonData(paths.localPrompts)

    init {
        prompts.addAll(data.load<List<Prompt>>(emptyList()))
    }

    override fun prompts(): List<Prompt> {
        return prompts
    }

    override fun info(): SourceInfo {
        return SourceInfo(
            "Local Storage", "A seed that stores prompts in local storage", "local-storage", "storage"
        )
    }

    fun savePrompt(prompt: PromptRequest) {
        require(prompt.sourceId == "local-storage") { "only local-storage supported" }
        prompts.add(
            Prompt(
                prompt.content, prompt.title, id(), prompt.sourceId, prompt.meta
            )
        )
        data.save(prompts)
    }
}


