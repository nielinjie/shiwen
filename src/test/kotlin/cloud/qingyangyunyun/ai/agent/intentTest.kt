package cloud.qingyangyunyun.ai.agent

import arrow.core.Either
import arrow.core.some
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IntentTests : StringSpec({
    val define = object :IntentsDefine {
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
    "satisfied"{
        val holding = IntentHolding(GotIntent("招呼").some(), emptyList())
        holding.satisfied(define) shouldBe true
    }
})