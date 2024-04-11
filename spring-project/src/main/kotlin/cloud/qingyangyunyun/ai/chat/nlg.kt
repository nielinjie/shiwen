package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import cloud.qingyangyunyun.custom.carInsurance.QueryResultItem
import org.springframework.stereotype.Component

sealed interface Request {
    object IntentRequest : Request
    object None : Request
    class Failed(val reason: String) : Request
    class QueryRequest(val propertyName: List<String>) : Request
    object Result : Request
}

enum class Format {
    Long, Short
}

fun QueryResultItem.byIntent(intent: Intent, format: Format): Pair<String,Int> {
    val values = when (intent.value) {
        "详细" -> listOf(
            carModel,
            area,
            carOwner,
            insuranceCompany,
            procedureFee1,
            procedureFee2,
            underwritingPolicy
        )

        "车主" -> listOf(carOwner)
        "地区" -> listOf(area)
        "公司" -> listOf(insuranceCompany)
        "车型" -> listOf(carModel)
        "费用" -> listOf(
            carModel,
            area,
            carOwner,
            insuranceCompany,
            procedureFee1,
            procedureFee2,
            underwritingPolicy
        )

        "承保政策" -> listOf(
            carModel,
            area,
            carOwner,
            insuranceCompany,
            procedureFee1,
            procedureFee2,
            underwritingPolicy
        )
        else -> listOf("")
    }.map { it ?: "" }

    return if (format == Format.Short) (values.joinToString(",") to 1) else {
        (values.joinToString("|", prefix = "|", postfix = "|") to values.size)
    }
}

@Component
class NLG() {
    fun result(result: QueryResult, intent: Intent): String {
        if(result.isEmpty()) return "无结果"
        val format = if (intent.value in listOf("公司", "车型","地区","车主")) Format.Short else Format.Long
        val recorder = result.map { it.byIntent(intent, format) .first}

        return when (format) {
            Format.Long -> {
                val columns = result.first().byIntent(intent, format).second
                val head = (1..columns).joinToString("|", prefix = "|", postfix = "|"){" "}
                val head2 = (1..columns).joinToString("|", prefix = "|", postfix = "|"){"-"}
                head+"\n"+head2+"\n"+
                recorder.joinToString("\n")

            }

            Format.Short -> {
                recorder.joinToString("、")
            }
        }
    }

    fun output(request: Request? = Request.None, input: String, session: Session): String {
        return when (request) {

            is Request.IntentRequest -> {
                "Sorry, 没有理解您的请求，请输入具体要查询的信息，比如公司、费率、承保政策"
            }

            is Request.None -> {
                "例子。。。"
            }

            is Request.Failed -> {
                request.reason + " - 失败，请稍后尝试。"
            }

            is Request.QueryRequest -> {
                "我可以根据你输入的查询条件进行查找，比如" + request.propertyName.ifEmpty { listOf("车型") }
                    .joinToString("、")
            }

            is Request.Result -> {
                ((session.state as? State.Chatting)?.intent?.let { intent ->
                    session.result()?.let {
                        result(it, intent)
                    }
                }) ?: "(无结果)"
            }

            null -> error("not support code")
        }
    }
}