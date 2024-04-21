package cloud.qingyangyunyun.ai.agent.car

import cloud.qingyangyunyun.ai.agent.*

val carTone = object : Tone {
    override fun help(): Output {
        return helps().md()
    }
}

private fun helps(): String {
    return "我的功能是协助处理车险咨询，您可以直接提问 - " nn
            "- 成都地区的新能源网约车可以出哪些公司？" n
            "- 地市区亚太燃油网约车的承保政策是？" n
            "- 商贸车主的车险费用是多少？" n
            "- 可以承保的车型有哪些？" n
            "- 地市区恒邦燃油网约车的费用是？"
}