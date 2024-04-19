package cloud.qingyangyunyun.ai.agent

import cloud.qingyangyunyun.ai.log.LogStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Session(
    @Autowired
    val define: Define,
    @Autowired
    val nlu: NLU,
    @Autowired
    val policy: Policy,
    @Autowired
    val logStore: LogStore) {
    var stateMachine: StateMachine = StateMachine(define)
    var history: History = History()
    var latestInput: Input? = null
    fun input(input: String) {
        val underStood = nlu.understand(input)
        logStore.add("input understood:" n underStood.toString())
        latestInput = Input.UserInput(underStood)
        this.history.appendUser(input)
        logStore.add("current state:" n stateMachine.currentState.toString())
        stateMachine.onEvent(Input.UserInput(underStood))
    }


    fun output(): Output {
        val action = policy.decide(this.stateMachine.currentState, latestInput)
        logStore.add("action is:" n action.toString())
        return when (action) {
            is Reply -> action
            else -> error("not supported")
        }.let {
            toOutput(it)
        }.also {
            this.history.appendAgent(it.body)
        }
    }
}