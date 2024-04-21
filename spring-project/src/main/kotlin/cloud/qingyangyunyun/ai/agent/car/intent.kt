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
                name = "目标",
                alias = listOf("字段"),
                jsonName = "target",
                type = "string",
                required = false,
                allowedValues = "费用、公司、承保政策、车型、地区、车主、核保要求、详细、有没有".list(),
                examples = "费用、公司、承保政策、车型、核保要求、车主、地区、详细、全部、有没有、可不可以、能不能".list(),
                description = "查询目标"
            ),
            SlotDef(
                name = "车型",
                alias = emptyList(),
                jsonName = "carModel",
                type = "string",
                required = false,
                allowedValues = "新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车".list(),
                examples = "新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车".list(),
                description = "查询车型"
            ),
            SlotDef(
                name = "地区",
                alias = emptyList(),
                jsonName = "area",
                type = "string",
                required = false,
                allowedValues = "成都、省内、成都以外、地市区".list(),
                examples = "成都、省内、成都以外、地市区".list(),
                description = "查询地区"
            ),
            SlotDef(
                name = "车主", alias = emptyList(),
                jsonName = "carOwner", type = "string",
                required = false,
                allowedValues = "个人、商贸、租赁".list(), examples = "个人、商贸、租赁".list(),
                description = "查询车主"
            ),
            SlotDef(
                name = "保险公司",
                alias = emptyList(),
                jsonName = "insuranceCompany",
                type = "string",
                required = false,
                allowedValues = companies,
                examples = companies,
                description = "查询保险公司"
            ),
            SlotDef(
                name = "数据集",
                alias = emptyList(),
                jsonName = "dataset",
                type = "string",
                required = false,
                allowedValues = "承运人责任".list(),
                examples = "承运人责任、承运人险、承运人、承运人责任险".list(),
                description = "查询数据集"
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
