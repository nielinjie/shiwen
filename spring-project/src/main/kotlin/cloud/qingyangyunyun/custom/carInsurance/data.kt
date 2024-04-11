package cloud.qingyangyunyun.custom.carInsurance

import cloud.qingyangyunyun.ai.chat.Intent
import org.springframework.stereotype.Component
import org.supercsv.io.CsvListReader
import org.supercsv.prefs.CsvPreference


/*
（车型、车主、保险公司、手续费、承保政策）
CarModel,CarOwner, InsuranceCompany, ProcedureFee, UnderwritingPolicy
 */
@Component
class Data {
    val companies: List<String> = javaClass.getResource(
        "/cloud/qingyangyunyun/custom/carInsurance/companies.txt"
    )!!.readText().lines().map {
        it.trim()
    }.distinct()
    val dataMatrix = dumpCsv("cloud/qingyangyunyun/custom/carInsurance/policy.csv")
    fun query(query: Query): QueryResult {
        return query.matcher().let {
            dataMatrix.filter { item ->
                it.match(item)
            }.map {
                fromCsv(it)
            }
        }
    }

    fun dumpCsv(path: String): List<List<String>> {
        val resource = javaClass.classLoader.getResourceAsStream(path) ?: error("no such resource")
        val result = mutableListOf<List<String>>()
        CsvListReader(resource.reader(), CsvPreference.STANDARD_PREFERENCE).use {
            var line: List<String>? = null
            while ((it.read().also { line = it }) != null) {
                result.add(line!!)
            }
        }
        return result
    }
}


data class Query(
    var carModel: String? = null,
    var area: String? = null,
    var insuranceCompany: String? = null,
    var carOwner: String? = null,

//    var procedureFee: String? =null,
//    var underwritingPolicy: String? =null
)

typealias QueryResult = List<QueryResultItem>

data class QueryResultItem(
    var carModel: String?,
    var area: String?,
    var insuranceCompany: String?,
    var carOwner: String?,

    var procedureFee1: String?,
    var procedureFee2: String?,
    var underwritingPolicy: String?
) {

}

fun fromCsv(line: List<String>): QueryResultItem {
    return QueryResultItem(
        carModel = line[0],
        area = line[1],
        insuranceCompany = line[2],
        carOwner = line[3],
        procedureFee1 = line[4],
        procedureFee2 = line[5],
        underwritingPolicy = line[6]
    )

}

fun main() {
    val data = Data()
    val result = data.dumpCsv("cloud/qingyangyunyun/custom/carInsurance/policy.csv")
    println(result)
}