package cloud.qingyangyunyun.ai.log

import arrow.core.raise.catch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import xyz.nietongxue.common.log.Log


class LogHandler(val store: LogStore) : TextWebSocketHandler() {
    private var session: WebSocketSession? = null

    init {
        store.listener = { log: Log<String> ->
            Json.encodeToString(log).also {
                try {
                    this.session?.sendMessage(TextMessage(it))
                }catch (_: Exception){
                }

            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        this.session = session
    }
}