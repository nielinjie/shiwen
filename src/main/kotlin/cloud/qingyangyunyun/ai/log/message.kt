package cloud.qingyangyunyun.ai.log

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.util.HtmlUtils
import xyz.nietongxue.common.log.Log


class HelloMessage(var name: String?) {


}

class Greeting(content: String?) {
    var content: String? = content


}


@Configuration
@EnableWebSocketMessageBroker
class STOMPWebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic","/queue")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/api/stomp").setAllowedOriginPatterns("*").withSockJS() //TODO 这里是否有安全问题？
    }
}

@Controller
class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: HelloMessage): Greeting {
        Thread.sleep(1000) // simulated delay
        return Greeting("Hello, " + HtmlUtils.htmlEscape(message.name!!) + "!")
    }
}

@Component
class LogSender(
    @Autowired val template: SimpMessagingTemplate
) {
    fun sendLog(log: Log<*>) {
        this.template.convertAndSend("/topic/logs", log)
    }

}