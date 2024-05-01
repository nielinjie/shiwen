package cloud.qingyangyunyun.ai.clients

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class Brief(
    val start: String,
    val duration: Long,
    val tokens: String,
)

val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())

@Serializable
data class ClientCallingRecord(
    val clientName: String,
    val prompt: String, val response: String,
    val promptTokens: Long, val generationTokens: Long,
    val start: Instant, val end: Instant,
) {
    val brief = Brief(
        start.let {
            formatter.format(it.toJavaInstant())
        },
        end.minus(start).inWholeMilliseconds,
        "$promptTokens/$generationTokens"
    )
}

@Component
class RecorderSender(
    @Autowired val template: SimpMessagingTemplate
) {
    fun send(record: ClientCallingRecord) {
        template.convertAndSend("/topic/records", record)
    }
}

@Component
class Recorder(@Autowired val listener: RecorderSender) {
    val records: MutableList<ClientCallingRecord> = mutableListOf()
    fun record(clientName: String, prompt: Prompt, response: ChatResponse, start: Instant, end: Instant) {
        ClientCallingRecord(
            clientName,
            prompt.toString(),
            response.toString(),
            response.metadata.usage.promptTokens,
            response.metadata.usage.generationTokens,
            start,
            end
        ).also {
            this.records.add(
                it
            )
            this.listener.send(it)
        }

    }
}

fun recorded(name: String, client: ChatClient, recorder: Recorder): ChatClient {
    return ChatClient { prompt ->
        val start = Clock.System.now()
        val response = client.call(prompt)
        val end = Clock.System.now()
        recorder.record(name, prompt, response, start, end)
        response
    }
}

@RestController
class RecordController(

    @Autowired(required = false)
    val recorder: Recorder?
) {
    @Serializable
    data class RecordResponse(val records: List<ClientCallingRecord>)

    @GetMapping("/api/records")
    fun getRecords(): RecordResponse {
        return RecordResponse(recorder?.records ?: emptyList())
    }

    @PostMapping("/api/records/reset")
    fun resetRecords() {
        recorder?.records?.clear()
    }
}