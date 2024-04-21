package cloud.qingyangyunyun.ai.agent

const val CHAT_NOT_UNDERSTOOD = "对不起，我不太明白您需要我做什么。"

const val CHAT_BYE = "谢谢，再见。"


fun slotsRequest(names: List<String>): String {
    return "请告诉我${names.joinToString("、")}"
}

fun intentRequest(): String {
    return CHAT_NOT_UNDERSTOOD
}



fun greeting(): String {
    return "你好。"
}

fun fallback(): String {
    return "对不起，我无法满足您的要求。"
}

fun bye(): String {
    return CHAT_BYE
}

