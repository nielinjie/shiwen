package cloud.qingyangyunyun.ai.matrix

import cloud.qingyangyunyun.ai.clients.ClientsService
import cloud.qingyangyunyun.ai.function.FunctionService
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class Service(
    @Autowired val clientsService: ClientsService, @Autowired val functionsService: FunctionService
) {


    fun call(
        prompt: String, variables: Map<String, String>, client: String, functions: List<String> = emptyList()
    ): String {
        val promptReplaced = replaceVariables(prompt, variables)
        val chatClient: ChatClient = clientsService.getClient(client)
        val options = OpenAiChatOptions.builder().withFunctionCallbacks(functionsService.callbacks(functions)).build()
        val promptObject = Prompt(promptReplaced, options)
        return chatClient.call(promptObject).result.output.content
    }

    fun getClients(): List<String> {
        return clientsService.getClients()
    }
}

//TODO set start and end delimiter, default is ${{xxx}}
fun replaceVariables(prompt: String, variables: Map<String, String>): String {
    var result = prompt
    variables.forEach { (k, v) ->
        result = result.replace("\${{$k}}", v)
    }
    return result
}


