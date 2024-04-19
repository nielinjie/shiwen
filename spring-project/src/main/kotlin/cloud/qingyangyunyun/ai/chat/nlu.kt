package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.ai.clients.ClientsService
import cloud.qingyangyunyun.custom.carInsurance.Data
import kotlinx.serialization.json.*
import net.sourceforge.htmlunit.corejs.javascript.Slot
import net.sourceforge.htmlunit.xpath.operations.Bool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component



sealed interface Understanding {
    data class Querying(val jsonElement: JsonElement) : Understanding {
        fun goon(): Boolean {
            return (this.jsonElement as? JsonObject)?.let {
                it["command"]?.jsonPrimitive?.content == "继续"
            } ?: false
        }
    }

    data class Command(val command: String) : Understanding
    data class NothingFind(val input: Input) : Understanding
    data class Failed(val message: String) : Understanding
}

class NLU(
    @Autowired val data: Data,
    @Autowired val clientsService: ClientsService
) {

    val instruction: String = """你的任务是识别用户的输入，用户输入可能是对车险产品的“选择条件”、“查询目的”，也可能是“操作指令”和“数据集指定”。
    车险产品的“选择条件”包括：车型(carModel)、地区(area)、车主(carOwner)、保险公司(insuranceCompany)。
    “查询目的”(intent)包括：费用、公司、承保政策、车型、详细、全部、有没有、可不可以、能不能。
    “操作指令”(command)可能是：其中、继续、深入、进一步、重新开始、清空、重来、功能、能干什么、问候、你好、拜拜、告别等。
    “数据集指定”(dataset)可能是：承运人责任险。
    
    根据用户输入，识别用户在上述方面的倾向。
""".trimIndent()
    val outputFormat: String = """以JSON格式输出。
    1. carModel字段的取值为string类型，取值必须是以下之一：新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车 或 null。
    2. area字段的取值为string类型，取值必须是以下之一：成都、省内、成都以外 或 null。
    3. carOwner字段的取值为string类型，取值必须是以下之一：个人、商贸、租赁 或 null。
    4. insuranceCompany字段的取值为string类型，取值必须是以下之一：${data.companies.joinToString("、")} 或 null。
    5. intent字段的取值为string类型，取值必须是以下之一：费用、公司、承保政策、车型、地区、车主、详细、全部、有没有 或 null。
    6. command字段的取值为string类型，取值必须是以下之一：继续、清空、招呼、告别、帮助 或 null。
    7. dataset字段的取值为string类型，取值必须是以下之一：承运人责任险 或 null。
    
    输出中只包含用户提及的字段，不要猜测任何用户未直接提及的字段。不要输出值为null的字段。
""".trimIndent()
    val examples: String = """1. 输入：新能源网约车的车险的费用如何？
    输出：{"carModel":"新能源网约车","intent":"费用"}
    
    2. 输入：成都可以保什么车型？
    输出：{"area":"成都","intent":"车型"}
    
    3. 输入：商贸车主的车险有哪些公司？
    输出：{"carOwner":"商贸","intent":"公司"}
    
    4. 输入：燃油货车省内的，平安有没有？
    输出：{"carModel":"新能源网约车","insuranceCompany":"平安"，"intent":"有没有"}
    
    5. 输入：其中有没有人保的？
    输出：{"command":"继续","insuranceCompany":"人保","intent":"有没有"}
    
    6. 输入：重新开始
    输出：{"command":"清空"}
    
    7. 输入：你好
    输出：{"command":"招呼"}
     
""".trimIndent()



    fun prompt(userInput: String): String {
        return """
    Instruction:
    $instruction
    
    OutputFormat:
    $outputFormat
    
    Examples:
    $examples
    
    UserInput:
    $userInput
""".trimIndent()
    }

    val client = "gpt4"
    fun parse(userInput: String): Understanding {
        val p = prompt(userInput)
        //可能的思路是template里面有其它的标记，比如`{{}}
//        val promptTemplate = PromptTemplate(p)
        return runCatching {
            clientsService.getClient(client)?.let {
                it.call(p).also {
                    println(it)
                }

            } ?: error("no client found - $client")
        }.flatmap {
            runCatching { Json.parseToJsonElement(it) }.recover {
                //有结果，但不是json
                JsonObject(mapOf())
            }
        }.fold(
            onSuccess = {
                understandingJson(it, userInput)
            },
            onFailure = {
                Understanding.Failed(it.message ?: "")
            }
        )
    }


}
fun understandingJson(
    it: JsonElement,
    userInput: String
) = if (it !is JsonObject || it.jsonObject.keys.isEmpty())
    Understanding.NothingFind(userInput)
else
    if (it.jsonObject.keys == setOf("command") && it.jsonObject["command"] != null)
        Understanding.Command(it.jsonObject["command"]!!.jsonPrimitive.content)
    else Understanding.Querying(it)
