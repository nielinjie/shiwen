package cloud.qingyangyunyun.ai.matrix

import cloud.qingyangyunyun.ai.clients.ClientsService
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class Service(@Autowired val clientsService: ClientsService) {


    fun call(prompt: String, variables: Map<String, String>, client: String): String {
        val promptTemplate = PromptTemplate(prompt, variables)
        return clientsService.getClient(client)?.let {
            it.call(promptTemplate.create()).result.output.content
        } ?: "no client found - $client"
    }

    fun getClients(): List<String> {
        return clientsService.getClients()
    }
}


