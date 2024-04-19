package cloud.qingyangyunyun.ai.agent

import arrow.core.Either


interface Define {
    val intentDefs: List<IntentDef>
    fun validateIntent(holding: IntentHolding): Either<String, IntentHolding>
    fun run(holding: IntentHolding,currentState: State): State
}



data class Example(val input: String, val output: String)
data class IntentDef(
    val name: String, val alias: List<String>,
    val slotDef: List<SlotDef>, val description: String,
    val examples: List<Example> = emptyList()
)

data class SlotDef(
    val name: String,
    val alias: List<String>,
    val jsonName:String,
    val type: String,
    val required: Boolean,
    val allowedValues: List<String>,
    val examples: List<String>,
    val description: String
)