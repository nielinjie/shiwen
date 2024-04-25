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






@Component
class LogSender(
    @Autowired val template: SimpMessagingTemplate
) {
    fun sendLog(log: Log<*>) {
        this.template.convertAndSend("/topic/logs", log)
    }

}