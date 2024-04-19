package cloud.qingyangyunyun.ai.agent

import arrow.core.None
import arrow.core.Option
import arrow.core.recover


data class IntentHolding(val intent: Option<GotIntent>, val slots: List<GotSlot>) {
    fun satisfied(define: Define) = define.validateIntent(this).isRight() //TODO use Either
    fun merge(b: IntentHolding): IntentHolding {
        return IntentHolding(
            intent.recover { b.intent.bind() }, (slots + b.slots).distinctBy { it.name }
        ) //TODO merge slots more carefully
    }

    fun reset() = copy(intent = None, slots = emptyList())
    fun hasIntent() = intent.isSome()
    fun slotValue(name: String) = slots.find { it.name == name }?.value
    fun slot(name: String) = slots.find { it.name == name }
}

data class GotIntent(val name: String)
data class GotSlot(val name: String, val value: String)