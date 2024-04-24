package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.ai.agent.Session
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

@Serializable
data class ChatMessage(val content: ChatContent, val sender: String, val time: Long)

@Serializable
data class ChatContent(val type: String, val body: String)

@Controller
class ChatController(
    @Autowired
    val factory: BeanFactory
) {


    @MessageMapping("/chats")
    @SendToUser("/queue/chats")
    @Throws(Exception::class)
    fun reply(message: ChatMessage, headerAccessor: SimpMessageHeaderAccessor): ChatMessage {
        val session: Session =
            (headerAccessor.sessionAttributes?.get("session") as? Session) ?: factory.getBean<Session>().also {
                // session should be scoped by user/websocket
                it.reset()
                headerAccessor.sessionAttributes?.put("session", it)
            }
        session.input(message.content.body)
        val output = session.output()
        return ChatMessage(ChatContent(output.type, output.body), "Bot", System.currentTimeMillis())
    }


}