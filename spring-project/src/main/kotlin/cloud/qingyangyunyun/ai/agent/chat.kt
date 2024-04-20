package cloud.qingyangyunyun.ai.agent

const val CHAT_NOT_UNDERSTOOD = "对不起，我不太明白您需要我做什么。"

const val CHAT_BYE = "谢谢，再见。"


fun slotsRequest(names: List<String>): String {
    return "请告诉我${names.joinToString("、")}"
}

fun intentRequest(): String {
    return CHAT_NOT_UNDERSTOOD
}

fun help(): String {
    return "我的功能是协助处理车险咨询，您可以这样查询 - \n" +
            "成都地区的新能源网约车可以出哪些公司？\n" +
            "地市区亚太燃油网约车的承保政策是？\n" +
            "商贸车主的车险费用是多少？\n" +
            "可以承保的车型有哪些？\n" +
            "地市区恒邦燃油网约车的费用是？\n"
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

