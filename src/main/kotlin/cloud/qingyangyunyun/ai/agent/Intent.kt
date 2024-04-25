package cloud.qingyangyunyun.ai.agent

import arrow.core.None
import arrow.core.Option
import arrow.core.Some




data class IntentHolding(val intent: Option<GotIntent>, val slots: List<GotSlot>) {
    fun satisfied(define: IntentsDefine) = define.validateIntent(this).isRight() //TODO use Either
    fun merge(b: IntentHolding, define: IntentsDefine): IntentHolding {
//        return IntentHolding(
//            intent.recover { b.intent.bind() }, (slots + b.slots).distinctBy { it.name }
//        )
        //TODO merge slots more carefully，有可能有些slot是可以进行叠加的。

        //TODO intent可能要具体处理，比如两个intent都有值，怎么处理，比如help作为intent，不能冲掉其他intent
        //TODO 有可能需要一个intent的历史？优先级？堆？
        //TODO 短时intent是否是另一种概念，command？
        //NOTE 目前处理为一个intent可能不holding，在replay directly中处理，见policy
        val newIntent = when (this.intent) {
            is None -> b.intent
            is Some -> when (b.intent) {
                is None -> this.intent
                is Some -> b.intent.value.let {
                    define.intentDef(it.name)?.let { def ->
                        if (def.holding) b.intent else this.intent
                    } ?: this.intent
                }
            }
        }
        return IntentHolding(
            newIntent, (b.slots + slots).distinctBy { it.name }
        )
    }

    fun reset() = IntentHolding(intent = None, slots = emptyList())
    fun hasIntent() = intent.isSome()
    fun slotValue(name: String) = slots.find { it.name == name }?.value
    fun slot(name: String) = slots.find { it.name == name }
}

data class GotIntent(val name: String)
data class GotSlot(val name: String, val value: String)


