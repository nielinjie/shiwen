package cloud.qingyangyunyun.ai.agent

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


interface Action {
}


interface Reply : Action {
    data object Fallback : Reply
    data object Ending : Reply
    data object Help : Reply
    data object Greeting : Reply
    data object IntentRequest : Reply
    data class SlotRequest(val names: List<String>) : Reply
    data class General(val text: String) : Reply
    data class Result(val data: cloud.qingyangyunyun.ai.agent.RunnerResult) : Reply
    data class Composite(val replies: List<Reply>) : Reply

    operator fun plus(reply: Reply): Reply {
        return when (this) {
            is Composite -> Composite(replies + reply)
            else -> Composite(listOf(this, reply))
        }
    }
}
@Component
class Policy(
//    @Autowired
//    val data: Data
) {
    fun decide(state: State, lastedInput: Input?): Action {

        var replyDirectly : Reply? = when(lastedInput){ //TODO replay 根据最后一个输入，需要与根据state的replay综合。
            is Input.UserInput -> {
                when(lastedInput.underStood){
                    is UnderStood.Intents -> {
                        Reply.IntentRequest
                    }
                    else -> null
                }
            }
            else -> null
        }

        return when (state) {
            is State.Start -> {
                Reply.Greeting
            }

            is State.WithSlot -> {
                Reply.IntentRequest
            }

            is State.WithIntent -> {
                Reply.SlotRequest(state.getMissSlots().map { it.name })
            }

            is State.Result -> {
                Reply.Result(state.result)
            }

            is State.Failed -> {
                Reply.Fallback
            }


            else -> {
                Reply.Help
            }
        }
    }
}