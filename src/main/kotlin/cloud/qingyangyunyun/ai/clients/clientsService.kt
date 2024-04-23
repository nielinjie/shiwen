package cloud.qingyangyunyun.ai.clients

import cloud.qingyangyunyun.ai.JsonData
import cloud.qingyangyunyun.ai.workshop.Paths
import cloud.qingyangyunyun.ai.workshop.WorkshopService
import cloud.qingyangyunyun.ai.workshop.Workspace
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.antlr.v4.runtime.misc.MultiMap
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatClient
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.openai.OpenAiChatClient
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ClientsService(
    @Autowired val workshopService: WorkshopService,
    @Autowired val paths: Paths,
    @Autowired(required = false) val recorder: Recorder? = null,
//    @Autowired(required = false) val cacheHolder: CacheHolder? = null //cache这个功能不应该有用。应该在更高层面cache。需要先高层验证结果的合理性。
) {
    val clients = mutableMapOf<String, ChatClient>()
    val apis = mutableMapOf<String, Any>()
    val configData = JsonData(paths.configPath)
    private var configs: ClientConfigs? = null

    val exampleConfigs = ClientConfigs(
        listOf(
            ApiConfig(
                "openai",
                "openai",
                "https://api.xxx.io/",
                "sk-xxxxxxxxxx"
            ),
            ApiConfig("ollama", "ollama", "http://localhost:11434", null)
        ),
        listOf(
            ClientConfig(
                "openai", "openai", mapOf(
                    "model" to "gpt-3.5-turbo",
                    "temperature" to "0.7"
                )
            ),
            ClientConfig(
                "qwen", "ollama", mapOf(
                    "model" to "qwen:14b",
                    "temperature" to "0.7"
                )
            )
        )
    )
    private val defaultConfigs = ClientConfigs(
        emptyList(), emptyList()
    )

    init {
        this.configs = load()
        fromApis(configs!!)
        fromConfigs(
            configs!!
        )
    }

    fun load(): ClientConfigs {
        return configData.load(defaultConfigs)
    }

    fun save() {
        configData.save(configs!!)
    }

    fun getClients(): List<String> {
        return clients.keys.toList()
    }

    fun getClient(client: String): ChatClient {
        return clients[client]!!
    }

    fun getConfigs(): ClientConfigs {
        return configs!!
    }

    fun setConfigs(configs: ClientConfigs) {
        this.configs = configs
        fromApis(configs)
        fromConfigs(configs)
        save()
    }

    private final fun fromApis(configs: ClientConfigs) {
        this.apis.clear()
        configs.apis.forEach {
            apis[it.name] = when (it.type) {
                "openai" -> OpenAiApi(it.url!!, it.key!!)
                "ollama" -> OllamaApi(it.url!!)
                else -> throw Exception("Unknown api type ${it.name}")
            }
        }
    }

    private final fun fromConfigs(configs: ClientConfigs) {
        clients.clear()
        configs.clients.forEach { clientConfig ->
            clients[clientConfig.name] = when (clientConfig.api) {
                "openai" -> OpenAiChatClient(
                    this.apis["openai"] as OpenAiApi,
                    OpenAiChatOptions.builder().apply {
                        clientConfig.config["model"]?.let { withModel(it) }
                        clientConfig.config["temperature"]?.let { withTemperature(it.toFloat()) }
                    }.build()
                )

                "ollama" -> OllamaChatClient(this.apis["ollama"] as OllamaApi).withDefaultOptions(
                    OllamaOptions().apply {
                        clientConfig.config["model"]?.let { withModel(it) }
                        clientConfig.config["temperature"]?.let { withTemperature(it.toFloat()) }
                    }
                )

                else -> throw Exception("Unknown client type ${clientConfig.api}")
            }.let {
                if (this.recorder != null) recorded(
                    clientConfig.name, it, this.recorder!!
                ) else it
            }
//                .let {
//                if (this.cacheHolder != null) cached(it, this.cacheHolder!!, clientConfig.name) else it
//            }
        }
    }

}

