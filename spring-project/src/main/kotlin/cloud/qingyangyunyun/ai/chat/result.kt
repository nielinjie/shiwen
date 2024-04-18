package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import cloud.qingyangyunyun.custom.carInsurance.QueryResultItem


enum class Format {
    Long, Short
}

fun QueryResultItem.byIntent(intent: Intent): List<String> {
    return when (intent.value) {
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

fun displayResult(result: QueryResult, intent: Intent?): String {
    val intentWithDefault = intent?: Intent("详细")
    if (result.isEmpty()) return "无结果"
    val format = if (intentWithDefault.value in listOf("公司", "车型", "地区", "车主")) Format.Short else Format.Long
    var columns = 0
    val recorder = result.map { it.byIntent(intentWithDefault) }.map {
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
            head + "\n" + head2 + "\n" +
                    recorder.joinToString("\n")
        }

        Format.Short -> {
            recorder.distinct().joinToString("、") //TODO 可能需要去除一些通配符。
        }
    }
}