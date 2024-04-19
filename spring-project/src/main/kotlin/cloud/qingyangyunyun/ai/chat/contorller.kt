package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.ai.agent.Session
import cloud.qingyangyunyun.ai.log.LogHandler
import cloud.qingyangyunyun.ai.log.LogStore
import kotlinx.serialization.encodeToString
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
//    val dialog: Dialog,
    val session: Session,
    @Autowired
    val logStore: LogStore
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(MyHandler(session), "/api/ws")
        registry.addHandler(
            LogHandler(logStore), "/api/logws"
        )

    }
}


class MyHandler(
//    val dialog: Dialog
    val session: Session
) : TextWebSocketHandler() {
    override fun handleTextMessage(webSocketSession: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val jsonElement = Json.parseToJsonElement(payload)
        val content = jsonElement.jsonObject["content"]?.jsonObject?.get("body")?.jsonPrimitive?.content
        content?.also {
            session.input(it)
            session.output().also { output ->
                webSocketSession.sendMessage(TextMessage(Json.encodeToString(output)))
            }
        }
        //session.sendMessage(TextMessage("Received: ${jsonElement.jsonObject["content"]?.jsonPrimitive?.content?:"(don't know)"}"))

    }
}