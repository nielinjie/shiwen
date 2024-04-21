package cloud.qingyangyunyun.ai.agent

import arrow.core.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StateTests : StringSpec({
    val define = object : IntentsDefine {
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
            return holding.intent.toEither { "no intent find" }.flatMap {
                when (it.name) {
                    "招呼" -> holding.right()
                    else -> "not support intent".left()
                }
            }
        }

        override fun run(holding: IntentHolding,currentState: State): State {
            return when (holding.intent.getOrNull()?.name) {
                "招呼" -> State.Start
                else -> State.Start
            }
        }
    }
    "start" {
        val machine = StateMachine(define)
        machine.currentState shouldBe State.Start
    }
    "on intents" {
        val machine = StateMachine(define)
        machine.onEvent(
            Input.UserInput(UnderStood.Intents(IntentHolding(GotIntent("招呼").some(), emptyList())))
        )
        machine.currentState shouldBe State.Start
    }
})
