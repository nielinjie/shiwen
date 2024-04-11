package cloud.qingyangyunyun.ai.chat

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig(
    @Autowired
    val dialog: Dialog
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(MyHandler(dialog), "/api/ws")
    }
}


class MyHandler(val dialog: Dialog) : TextWebSocketHandler() {
    override fun handleTextMessage(webSocketSession: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val jsonElement = Json.parseToJsonElement(payload)
        val content = jsonElement.jsonObject["content"]?.jsonPrimitive?.content
        content?.also {
            dialog.run(it).also {
                webSocketSession.sendMessage(TextMessage(it))
            }
        }
        //session.sendMessage(TextMessage("Received: ${jsonElement.jsonObject["content"]?.jsonPrimitive?.content?:"(don't know)"}"))

    }
}