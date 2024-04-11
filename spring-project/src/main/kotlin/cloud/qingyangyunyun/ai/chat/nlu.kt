package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.ai.clients.ClientsService
import cloud.qingyangyunyun.custom.carInsurance.Data
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


sealed interface Understanding {
    class Json(val jsonElement: JsonElement) : Understanding
    class NothingFind(val input: Input) : Understanding
    class Failed(val message: String) : Understanding
}

@Component
class NLU(
    @Autowired val data: Data,
    @Autowired val clientsService: ClientsService
) {

    val instruction: String = """你的任务是识别用户对车险产品的选择条件和查询目的。
    车险产品的选择条件包括：车型(carModel)、地区(area)、车主(carOwner)、保险公司(insuranceCompany)。
    查询目的(intent)包括：费用、公司、承保政策、车型。
    根据用户输入，识别用户在上述查询条件上的倾向。
""".trimIndent()
    val outputFormat: String = """以JSON格式输出。
    1. carModel字段的取值为string类型，取值必须是以下之一：新能源网约车、新能源私家车、燃油网约车、燃油私家车、燃油货车、新能源货车 或 null。
    2. area字段的取值为string类型，取值必须是以下之一：成都、省内、成都以外 或 null。
    3. carOwner字段的取值为string类型，取值必须是以下之一：个人、商贸、租赁 或 null。
    4. insuranceCompany字段的取值为string类型，取值必须是以下之一：${data.companies.joinToString("、")} 或 null。
    5. intent字段的取值为string类型，取值必须是以下之一：费用、公司、承保政策、车型、地区、车主、详细、全部 或 null。
    
    输出中只包含用户提及的字段，不要猜测任何用户未直接提及的字段。不要输出值为null的字段。
""".trimIndent()
    val examples: String = """1. 输入：新能源网约车的车险的费用如何？
    输出：{"carModel":"新能源网约车","intent":"费用"}
    
    2. 输入：成都可以保什么车型？
    输出：{"area":"成都","intent":"车型"}
    
    3. 输入：商贸车主的车险有哪些公司？
    输出：{"carOwner":"商贸","intent":"公司"}
    
    4. 输入：我想买平安的车险，租赁的。
    输出：{"insuranceCompany":"平安","carOwner":"租赁"}
    
    5. 输入：燃油货车省内的，平安有没有？
    输出：{"carModel":"新能源网约车","insuranceCompany":"平安"}
     
""".trimIndent()
    val userInput = """
    
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
            val result = clientsService.getClient(client)?.let {
                it.call(p).also {
                    println(it)
                }

            } ?: error("no client found - $client")
            Json.parseToJsonElement(result) //TODO retry if not a json.
        }.fold(
            onSuccess = {
                if (it.jsonObject.keys.isEmpty()) Understanding.NothingFind(userInput)
                Understanding.Json(it)
            },
            onFailure = {
                Understanding.Failed(it.message ?: "")
            }
        )
    }
}

