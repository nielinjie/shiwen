package cloud.qingyangyunyun.ai.agent.car

import cloud.qingyangyunyun.ai.agent.Output
import cloud.qingyangyunyun.ai.agent.markdown
import cloud.qingyangyunyun.ai.agent.plain
import cloud.qingyangyunyun.ai.chat.Format
import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import cloud.qingyangyunyun.custom.carInsurance.QueryResultItem


fun String.list(): List<String> {
    val delimiters = listOf("、","，")
    val results = delimiters.map { this.split(it).map { it.trim() } }
    return results.maxBy { it.size }
}

fun displayResult(result: QueryResult, target: String?): Output {
    val targetWithDefault: String = target ?: "详细"
    if (result.isEmpty()) return "无结果".plain()
    val format = if (targetWithDefault in listOf("公司", "车型", "地区", "车主")) Format.Short else Format.Long
    var columns = 0
    val recorder = result.map { it.byIntent(targetWithDefault) }.map {
        if (columns == 0) columns = it.size
        when (format) {
            Format.Long -> it.joinToString("|", prefix = "|", postfix = "|")
            Format.Short -> it.joinToString("、")
        }
    }

    return when (format) {
        Format.Long -> {
            val head = (1..columns).joinToString("|", prefix = "|", postfix = "|") { " " }
            val head2 = (1..columns).joinToString("|", prefix = "|", postfix = "|") { "-" }
            markdown(
                head + "\n" + head2 + "\n" +
                        recorder.joinToString("\n")
            )
        }

        Format.Short -> {
            recorder.distinct().joinToString("、").plain() //TODO 可能需要去除一些通配符。
        }
    }
}

fun QueryResultItem.byIntent(target: String): List<String> {
    return when (target) {
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
}