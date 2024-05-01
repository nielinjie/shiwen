package cloud.qingyangyunyun.ai.clients

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
class ClientConfigController(@Autowired val clientsService: ClientsService) {

    @GetMapping("/api/clients")
    fun getClients(): List<String> {
        return clientsService.getClients()
    }

    @GetMapping("/api/configs")
    fun getConfigs(): ClientConfigs {
        return clientsService.getConfigs()
    }


    @PostMapping("/api/configs")
    fun setConfigs(@RequestBody configs: ConfigsRequest) {
        try {
            Json.decodeFromString<ClientConfigs>(configs.string).let {
                clientsService.setConfigs(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException(e)
        }
    }

    @GetMapping("/api/configs/example")
    fun exampleConfigs(): ClientConfigs {
        return clientsService.exampleConfigs
    }
}

@Serializable
data class ConfigsRequest(
    val string: String
)


@Serializable
data class ClientConfig(
    val name: String,
    val api: String,
    val type: String = "chat",
    val default:Boolean = false,
    val config: Map<String, String>
)

@Serializable
data class ApiConfig(
    val name: String,
    val type: String,
    val url: String?,
    val key: String?
)

@Serializable
data class ClientConfigs(
    val apis: List<ApiConfig>,
    val clients: List<ClientConfig>
)
