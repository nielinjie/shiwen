package cloud.qingyangyunyun.ai.agent

import arrow.core.toOption
import cloud.qingyangyunyun.ai.chat.flatmap
import cloud.qingyangyunyun.ai.log.LogStore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.ai.chat.ChatClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


interface UnderStood {
    data class Intents(val holding: IntentHolding) : UnderStood {
        companion object {
            fun fromJson(json: JsonElement): Intents? {
                return (json as? JsonObject)?.let {
                    val intent = json["intent"]?.jsonPrimitive?.content?.let {
                        GotIntent(it)
                    }
                    val slots = json.keys.minus("intent").map {
                        GotSlot(it, json[it]?.jsonPrimitive?.content ?: "")
                    }
                    Intents(IntentHolding(intent.toOption(), slots))
                }
            }
        }
    }

    data class General(val text: String) : UnderStood
    data class Failed(val message: String) : UnderStood
}

@Component
class NLU(
    @Autowired
    val chatDefine: Define,
    @Autowired
    val chatClient: ChatClient,
    @Autowired
    val logStore: LogStore
) {
    fun understand(text: String): UnderStood {
        val prompt = promptForIntents(chatDefine.intentDefs) nn "UserInput:" n text
        return runCatching {
            chatClient.call(prompt).also {
                logStore.add("chatClient returned" n it)
            }
        }.flatmap {
            runCatching { Json.parseToJsonElement(it) }.recover {
                //有结果，但不是json
                JsonObject(mapOf())
            }
        }.fold(
            onSuccess = {
                UnderStood.Intents.fromJson(it) ?: UnderStood.General(text)
            },
            onFailure = {
                UnderStood.Failed(it.message ?: "")
            }
        )

    }
}

