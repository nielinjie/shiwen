package cloud.qingyangyunyun.ai.agent

import cloud.qingyangyunyun.ai.agent.car.TERM_HELP
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

    operator fun plus(reply: Reply?): Reply {
        return when (this) {
            is Composite -> reply?.let { Composite(replies + it) } ?: this
            else -> Composite(reply?.let { listOf(this, it) } ?: listOf(this))
        }
    }
}


fun Input.useIntents(): GotIntent? {
    return when (this) {
        is Input.UserInput -> when (this.underStood) {
            is UnderStood.Intents -> this.underStood.holding.intent.getOrNull()
            else -> null
        }

        else -> null
    }
}

@Component
class Policy(
//    @Autowired
//    val data: Data
) {
    fun decide(state: State, lastedInput: Input?): Action {

        val replyDirectly: Reply? = lastedInput?.useIntents()?.let {
            val name = it.name
            when (name) {
                TERM_HELP -> Reply.Help
                else -> null
            }
        }

        val stateReply: Reply? = when (state) {
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
                null
            }
        }
        return when (replyDirectly) {
            null -> stateReply ?: Reply.Fallback
            else -> replyDirectly + stateReply
        }
    }
}