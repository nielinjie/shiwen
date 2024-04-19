package cloud.qingyangyunyun.ai.agent

fun promptForIntents(intentDefs: List<IntentDef>): String {
    return """
        Instruction:
        你是一个客服人员，你的任务是根据用户的输入，识别用户的意图，然后根据用户的意图，提取用户的槽值。
        <意图可能有：>""".trimIndent() n
            intentDefs.mapIndexed { index, intent -> promptIntent(index, intent) }.n n
            "<槽值可能有：>" n
            intentDefs.flatMap { it.slotDef.map { s -> it to s } }
                .mapIndexed() { index, (intent, slot) -> promptSlot(index, intent, slot) }.n nn
            promptJson(intentDefs) nn
            promptExample(intentDefs)

}

fun promptExample(intentDefs: List<IntentDef>): String {
    return "Examples:" nn
            intentDefs.flatMap { it.examples }.map {
                "输入：${it.input}\n" + "输出：${it.output}"
            }.nn
}

fun promptJson(intentDefs: List<IntentDef>): String {
    return "OutputFormat:" n "以JSON格式输出。" n
            "1. intent字段取值为string类型，取值必须是一下之一：${intentDefs.joinToString("、") { it.name }}" n
            intentDefs.flatMap { it.slotDef.map { s -> it to s } }
                .mapIndexed() { index, (intent, slot) ->
                    promptJsonForSlot(
                        index + 1,
                        slot
                    )
                }.n nn
            "输出中只包含用户提及的字段，不要猜测任何用户未直接提及的字段。不要输出值为null的字段。"
}

fun promptIntent(index: Int, intentDef: IntentDef): String {
    return "${index + 1}. “${intentDef.name}”可能被提及为：${intentDef.alias.joinToString("、")}。"
}

fun promptSlot(index: Int, intentDef: IntentDef, slotDef: SlotDef): String {
    return "${index + 1}. “${slotDef.name}”（${slotDef.jsonName}）" +
            if (slotDef.examples.isNotEmpty())
                "可能包括：${slotDef.examples.joinToString("、")}。"
            else ""
}

fun promptJsonForSlot(index: Int, slotDef: SlotDef): String {
    return """${index + 1}. ${slotDef.jsonName}字段为${slotDef.type}类型。""" +
            if (slotDef.allowedValues.isNotEmpty()) "取值必须是以下之一：${slotDef.allowedValues.joinToString("、")} 或 null。" else ""
}