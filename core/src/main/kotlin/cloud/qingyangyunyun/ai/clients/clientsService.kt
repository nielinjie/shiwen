package cloud.qingyangyunyun.ai.clients

import cloud.qingyangyunyun.ai.JsonData
import cloud.qingyangyunyun.ai.Paths
import cloud.qingyangyunyun.ai.workshop.WorkshopService
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.document.MetadataMode
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.ollama.OllamaChatClient
import org.springframework.ai.ollama.OllamaEmbeddingClient
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.openai.OpenAiChatClient
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.OpenAiEmbeddingClient
import org.springframework.ai.openai.OpenAiEmbeddingOptions
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.retry.RetryUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ClientsService(
    @Autowired val workshopService: WorkshopService,
    @Autowired val paths: Paths,
    @Autowired(required = false) val recorder: Recorder? = null,
//    @Autowired(required = false) val cacheHolder: CacheHolder? = null //cache这个功能不应该有用。应该在更高层面cache。需要先高层验证结果的合理性。
) {
    val clients = mutableMapOf<ClientConfig, ChatClient>()
    val embeddingClients = mutableMapOf<ClientConfig, EmbeddingClient>()
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
            ApiConfig("ollama", "ollama", "http://localhost:11434", null),
        ),
        listOf(
            ClientConfig(
                "gpt35", "openai", default = true, config = mapOf(
                    "model" to "gpt-3.5-turbo",
                    "temperature" to "0.7"
                )
            ),
            ClientConfig(
                "qwen", "ollama", config = mapOf(
                    "model" to "qwen:14b",
                    "temperature" to "0.7"
                )
            ),
            ClientConfig(
                "nomic", "ollama", "embedding", default = true, mapOf(
                    "model" to "nomic-embed-text",
                )
            ),
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
        return clients.keys.map { it.name }
    }

    fun getClient(client: String): ChatClient {
        return clients.filter { it.key.name == client }.values.firstOrNull() ?: error("no such client found - $client")
    }

    fun getDefaultClient(): ChatClient {
        return clients.filter { it.key.default }.values.firstOrNull() ?: error("no default client found")
    }

    fun getEmbeddingClient(client: String): EmbeddingClient {
        return embeddingClients.filter { it.key.name == client }.values.firstOrNull()
            ?: error("no such embedding client found - $client")
    }

    fun getDefaultEmbeddingClient(): EmbeddingClient {
        return embeddingClients.filter { it.key.default }.values.firstOrNull()
            ?: error("no default embedding client found")
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
        configs.clients.filter { it.type == "chat" }.forEach { clientConfig ->
            clients[clientConfig] = when (clientConfig.api) {
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
        configs.clients.filter { it.type == "embedding" }.forEach { clientConfig ->
            embeddingClients[clientConfig] = when (clientConfig.api) {
                "ollama" -> OllamaEmbeddingClient(
                    this.apis["ollama"] as OllamaApi
                ).withDefaultOptions(
                    OllamaOptions().apply {
                        clientConfig.config["model"]?.let { withModel(it) }
                    }
                )

                "openai" -> OpenAiEmbeddingClient(
                    this.apis["openai"] as OpenAiApi, MetadataMode.EMBED,
                    OpenAiEmbeddingOptions.builder().apply {
                        clientConfig.config["model"]?.let { withModel(it) }
                    }.build(), RetryUtils.DEFAULT_RETRY_TEMPLATE
                )

                else -> throw Exception("Unknown client type ${clientConfig.api}")
            }
        }
    }

}

