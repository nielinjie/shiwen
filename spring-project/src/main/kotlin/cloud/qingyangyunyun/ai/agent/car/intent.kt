package cloud.qingyangyunyun.ai.agent.car

import cloud.qingyangyunyun.ai.agent.Example
import cloud.qingyangyunyun.ai.agent.IntentDef
import cloud.qingyangyunyun.ai.agent.SlotDef
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

val companies: List<String> = Any::class::class.java.getResource(
    "/cloud/qingyangyunyun/custom/carInsurance/companies.txt"
)!!.readText().lines().map {
    it.trim()
}.distinct()

val defs = listOf(
    IntentDef(
        "查询",
        listOf("查找", "search"),
        listOf(
            SlotDef(
                "目标",
                listOf("字段"),
                "target",
                "string",
                false,
                "费用、公司、承保政策、车型、地区、车主、详细、全部、有没有".list(),
                "费用、公司、承保政策、车型、详细、全部、有没有、可不可以、能不能".list(),
                "查询目标"
            ),
            SlotDef(
                "车型",
                emptyList(),
                "carModel",
                "string",
                false,
                "新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车".list(),
                "新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车".list(),
                "查询车型"
            ),
            SlotDef(
                "地区", emptyList(), "area", "string", false,
                "成都、省内、成都以外、地市区".list(), "成都、省内、成都以外、地市区".list(), "查询地区"
            ),
            SlotDef(
                "车主", emptyList(), "carOwner", "string", false,
                "个人、商贸、租赁".list(), "个人、商贸、租赁".list(),
                "查询车主"
            ),
            SlotDef(
                "保险公司", emptyList(), "insuranceCompany", "string", false,
                companies, companies, "查询保险公司"
            ),
            SlotDef("数据集",
                emptyList(),
                "dataset",
                "string",
                false,
                "承运人责任".list(),
                "承运人责任、承运人、承运人责任险".list(),
                "查询数据集"
            )
        ),
        "查询数据",
        listOf(
            Example("查询成都市公司", (buildJsonObject {
                put("intent", "查询")
                put("target", "公司")
            }).toString())
        )
    ),
    IntentDef(
        "重置",
        listOf("reset"),
        emptyList(),
        "重置查询条件",
        listOf(
            Example("重置", (buildJsonObject {
                put("intent", "重置")
            }).toString())
        )
    ),
    IntentDef(
        "帮助", listOf("help"), emptyList(), "帮助",
        listOf(
            Example("帮助", (buildJsonObject {
                put("intent", "帮助")
            }).toString())
        ),
        holding = false
    )

)
