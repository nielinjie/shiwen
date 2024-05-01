package cloud.qingyangyunyun.ai.embedding

import cloud.qingyangyunyun.ai.clients.ClientsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EmbeddingService(@Autowired val clientsService: ClientsService) {
    fun embed(text: String, clientName: String): List<Double> {
        try {
            clientsService.getEmbeddingClient(clientName).let {
                return it.embed(text)
            }
        } catch (e: Exception) {
            error("error: ${e.message}")
        }
    }
}