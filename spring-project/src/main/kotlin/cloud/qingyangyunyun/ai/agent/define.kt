package cloud.qingyangyunyun.ai.agent

import arrow.core.Either
import arrow.core.left
import arrow.core.right


interface Define {
    val intentDefs: List<IntentDef>
    fun validateIntent(holding: IntentHolding): Either<String, IntentHolding>{
        return when (val intentName = holding.intent.getOrNull()?.name) {
            null -> "no intent  find".left()

            else -> {
                if (this.intentDef(intentName) == null) {
                    "intent define not found".left()
                } else {
                    holding.right()
                }
            }
        }
    }
    fun run(holding: IntentHolding, currentState: State): State
    fun intentDef(name: String): IntentDef? = intentDefs.find { it.name == name }
}


data class Example(val input: String, val output: String)
data class IntentDef(
    val name: String, val alias: List<String>,
    val slotDef: List<SlotDef>, val description: String,
    val examples: List<Example> = emptyList(),
    val holding: Boolean = true
)

data class SlotDef(
    val name: String,
    val alias: List<String>,
    val jsonName: String,
    val type: String,
    val required: Boolean,
    val allowedValues: List<String>,
    val examples: List<String>,
    val description: String
)