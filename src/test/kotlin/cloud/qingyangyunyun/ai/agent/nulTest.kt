package cloud.qingyangyunyun.ai.agent

import arrow.core.Either
import cloud.qingyangyunyun.ai.clients.ClientsService
import cloud.qingyangyunyun.ai.log.LogStore
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.chat.prompt.Prompt


class NLUTest : StringSpec({
    val logStore = mockk<LogStore>()
    val chatDefine = object : IntentsDefine {
        override val intentDefs: List<IntentDef>
            get() = (
                    listOf(
                        IntentDef(
                            "招呼",
                            listOf("你好", "hello"),
                            listOf(
                                SlotDef(
                                    "姓名",
                                    listOf("我的名字", "我叫"),
                                    "name",
                                    "string",
                                    true,
                                    emptyList(),
                                    emptyList(),
                                    "用户的名字"
                                )
                            ),
                            "想要打个招呼",
                            listOf(
                                Example("你好，我是小智", """{"intent": "招呼", "name": "小智"}"""),
                                Example("hello", """{"intent": "招呼"}""")
                            )
                        )
                    )
                    )

        override fun validateIntent(holding: IntentHolding): Either<String, IntentHolding> {
            TODO("Not yet implemented")
        }

        override fun run(holding: IntentHolding, currentState: State): State {
            TODO("Not yet implemented")
        }
    }
    "prompts" {
        val prompt = promptForIntents(chatDefine.intentDefs)
        println(prompt)
    }
    "understand" {
        val client = mockk<ClientsService>()
        every { client.getClient("gpt35") } returns chatClientReturnJson(buildJsonObject {
            put("intent", "招呼")
            put("name", "小智")
        })

        val nlu = NLU(chatDefine, client, logStore,null)
        val result = nlu.understand("你好，我是小智")
        result.shouldBeInstanceOf<UnderStood.Intents>().also {
            it.holding.intent.shouldBe(GotIntent("招呼"))
            it.holding.slots.shouldContainExactly(GotSlot("name", "小智"))
        }
    }
    "understand with out intent" {

        val client = mockk<ClientsService>()
        every { client.getClient("gpt35") } returns chatClientReturnJson(buildJsonObject {
            put("name", "小智")
        })

        val nlu = NLU(chatDefine, client, logStore,null)
        val result = nlu.understand("你好，我是小智")
        result.shouldBeInstanceOf<UnderStood.Intents>().also {
            it.holding.intent.shouldBeNull()
            it.holding.slots.shouldContainExactly(GotSlot("name", "小智"))
        }
    }
})

fun chatClientReturnJson(json: JsonElement): ChatClient {
    return object : ChatClient {
        override fun call(prompt: String): String {
            return json.toString()
        }

        override fun call(prompt: Prompt?): ChatResponse {
            TODO("Not yet implemented")
        }
    }
}