package cloud.qingyangyunyun.ai.clients

import cloud.qingyangyunyun.ai.workshop.WorkshopService
import cloud.qingyangyunyun.ai.workshop.Workspace
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.ollama.OllamaChatClient
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.openai.OpenAiChatClient
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ClientsService(@Autowired val workshopService: WorkshopService) {
    val clients = mutableMapOf<String, ChatClient>()
    val apis = mutableMapOf<String, Any>()
    private var configs: ClientConfigs? = null


    private val defaultConfigs = ClientConfigs(
        emptyList(), emptyList()
//        listOf(
//            ApiConfig(
//                "openai",
//                "openai",
//                "https://api.xxx.io/",
//                "sk-xxx"
//            ),
//            ApiConfig("ollama", "ollama", "http://localhost:11434", null)
//        ),
//        listOf(
//            ClientConfig(
//                "openai", "openai", mapOf(
//                    "model" to "gpt-3.5-turbo",
//                    "temperature" to "0.7"
//                )
//            ),
//            ClientConfig(
//                "qwen", "ollama", mapOf(
//                    "model" to "qwen:14b",
//                    "temperature" to "0.7"
//                )
//            )
//        )
    )

    init {
        this.configs = load()
        fromApis(configs!!)
        fromConfigs(
            configs!!
        )
    }


    fun load(): ClientConfigs {
        val path = workshopService.configPath
        val json = try {
            path.readText()
        } catch (e: Exception) {
            return defaultConfigs
        }
        return Json.decodeFromString(ClientConfigs.serializer(), json)
    }

    fun save() {
        val path = workshopService.configPath
        val json = Json.encodeToString(configs!!)
        path.writeText(json)
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
        configs.clients.forEach {
            clients[it.name] = when (it.api) {
                "openai" -> OpenAiChatClient(
                    this.apis["openai"] as OpenAiApi,
                    OpenAiChatOptions.builder().apply {
                        it.config["model"]?.let { withModel(it) }
                        it.config["temperature"]?.let { withTemperature(it.toFloat()) }
                    }.build()
                )

                "ollama" -> OllamaChatClient(this.apis["ollama"] as OllamaApi).withDefaultOptions(
                    OllamaOptions().apply {
                        it.config["model"]?.let { withModel(it) }
                        it.config["temperature"]?.let { withTemperature(it.toFloat()) }
                    }
                )

                else -> throw Exception("Unknown client type ${it.api}")
            }
        }
    }
}

