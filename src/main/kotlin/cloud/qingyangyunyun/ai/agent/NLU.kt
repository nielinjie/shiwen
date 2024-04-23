package cloud.qingyangyunyun.ai.agent

import arrow.core.toOption
import cloud.qingyangyunyun.ai.clients.CacheHolder
import cloud.qingyangyunyun.ai.clients.ClientsService
import cloud.qingyangyunyun.ai.log.LogStore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.ai.chat.ChatClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


fun <T, U> Result<T>.flatmap(f: (T) -> Result<U>): Result<U> {
    return when {
        this.isSuccess -> f(this.getOrNull()!!)
        this.isFailure -> return Result.failure(this.exceptionOrNull()!!)
        else -> throw IllegalStateException("Result is neither success nor failure")
    }
}

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
    val chatDefine: IntentsDefine,
    @Autowired
    val clientsService: ClientsService,
    @Autowired
    val logStore: LogStore,
    @Autowired(required = false)
    val cacheHolder: CacheHolder<String, UnderStood>?
) {
    fun understand(text: String): UnderStood {
        val prompt = promptForIntents(chatDefine.intentDefs) nn "UserInput:" n text
        return cacheHolder.getOrPut(prompt) {
            runCatching {
                clientsService.getClient("gpt35").call(prompt).also {
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
}
fun CacheHolder<*,*>?.getOrPut(prompt: String, defaultValue: () -> UnderStood): UnderStood {
    return this?.getOrPut(prompt) { defaultValue() } ?: defaultValue()
}

