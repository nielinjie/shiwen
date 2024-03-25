package cloud.qingyangyunyun.ai

import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.ollama.OllamaChatClient
import org.springframework.ai.openai.OpenAiChatClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class Service(@Autowired val ollamaClient: OllamaChatClient, @Autowired val openAiClient: OpenAiChatClient) {

    val clientMap: Map<String, ChatClient> = mapOf("qwen" to ollamaClient, "openai" to openAiClient)

    fun call(prompt: String, variables: Map<String, String>, client: String): String {
        val promptTemplate = PromptTemplate(prompt, variables)
        return clientMap[client]?.let {
            it.call(promptTemplate.create()).result.output.content
        } ?: "no client found - $client"
    }

    fun getClients(): List<String> {
        return clientMap.keys.toList()
    }
}


