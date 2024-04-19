package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.agent.Define
import cloud.qingyangyunyun.ai.agent.car.carDefine
import cloud.qingyangyunyun.ai.clients.ClientsService
import org.springframework.ai.chat.ChatClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackages = ["cloud.qingyangyunyun"])
class DemoApplication() {

    @Autowired
    lateinit var clientsService: ClientsService

    @Bean
    fun def(): Define {
        return carDefine
    }

    @Bean
    fun chatClient(): ChatClient {
        return clientsService.getClient("gpt35")
    }
}


fun main(args: Array<String>) {
    runApplication<DemoApplication>()
}
