package cloud.qingyangyunyun.ai.chat

import org.springframework.stereotype.Component

sealed interface Replay {
    data object IntentRequest : Replay
    data class Blah(val blah: String) : Replay //回复一些巴拉巴拉的废话
    data object Helping : Replay
    data class Failed(val reason: String) : Replay
    data class QueryRequest(val propertyName: List<String>) : Replay
    data object Result : Replay
    data object ShortedResult : Replay
    data class Composite(val replays: List<Replay>) : Replay

    fun and(other: Replay): Replay {
        return when (this) {
            is Composite -> Composite(replays + other)
            else -> Composite(listOf(this, other))
        }
    }
}


@Component
class NLG() {


    fun output(replay: Replay = Replay.Helping, input: String, session: Session): String {
        return when (replay) {
            is Replay.Composite -> {
                replay.replays.joinToString("\n") { output(it, input, session) }
            }

            is Replay.IntentRequest -> {
                "抱歉, 没有理解您的请求，请输入具体要查询的信息，比如公司、费率、承保政策."
            }

            is Replay.Helping -> {
                "我的功能是协助处理车险咨询，您可以这样查询 - \n" +
                        "成都地区的新能源网约车可以出哪些公司？\n" +
                        "地市区亚太燃油网约车的承保政策是？\n" +
                        "商贸车主的车险费用是多少？\n" +
                        "可以承保的车型有哪些？\n" +
                        "地市区恒邦燃油网约车的费用是？\n"

            }

            is Replay.Blah -> {
                replay.blah
            }

            is Replay.Failed -> {
                replay.reason + " - 失败，请稍后尝试。"
            }

            is Replay.QueryRequest -> {
                "可以输入的查询条件进一步查找，比如:  " + replay.propertyName.ifEmpty { listOf("车型") }
                    .joinToString("、")
            }

            is Replay.Result -> {
                (((session.state as? State.Chatting)?.intent.let { intent ->
                    displayResult(session.result(), intent)
                }))
            }

            is Replay.ShortedResult -> {
                "以下是部分示例结果: \n\n" +
                        (((session.state as? State.Chatting)?.intent.let { intent ->
                            session.result().take(3).let {
                                displayResult(it, intent)
                            }
                        }))
            }

        }
    }
}