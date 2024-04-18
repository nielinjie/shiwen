package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.chat.Understanding
import cloud.qingyangyunyun.ai.chat.understandingJson
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class UnderstandTest : StringSpec({
    "json command" {
        val json = JsonObject(mapOf("command" to JsonPrimitive("继续")))
        val understand = understandingJson(json, "") shouldBe Understanding.Command("继续")
    }
    "json command other" {
        val json = JsonObject(mapOf("command" to JsonPrimitive("清空")))
        val understand = understandingJson(json, "") shouldBe Understanding.Command("清空")
    }
    "json querying" {
        val json = JsonObject(mapOf("command" to JsonPrimitive("继续"), "intent" to JsonPrimitive("费用")))
        (understandingJson(json, "") shouldBe Understanding.Querying(json)).also {
            it as Understanding.Querying
            it.goon() shouldBe true
        }
    }
    "json querying other" {
        val json = JsonObject(mapOf("command" to JsonPrimitive("清空"), "intent" to JsonPrimitive("费用")))
        (understandingJson(json, "") shouldBe Understanding.Querying(json)).also {
            it as Understanding.Querying
            it.goon() shouldBe false
        }
    }
})