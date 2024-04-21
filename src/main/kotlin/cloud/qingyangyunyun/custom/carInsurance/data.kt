package cloud.qingyangyunyun.custom.carInsurance

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
    val dataMatrix2 = dumpCsv("cloud/qingyangyunyun/custom/carInsurance/policy2.csv")
    fun query(query: Query?): QueryResult {
        if (query == null) return QueryResult(dataMatrix.map { fromCsv(it) })
        return query.matcher().let {
            dataMatrix.filter { item ->
                it.match(item)
            }.map {
                fromCsv(it)
            }.let { QueryResult(it) }
        }
    }
    fun query2(query: Query2?): QueryResult2 {
        if (query == null) return QueryResult2(dataMatrix2.map { fromCsv2(it) })
        return query.matcher().let {
            dataMatrix2.filter { item ->
                it.match(item)
            }.map {
                fromCsv2(it)
            }.let{ QueryResult2(it) }
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
    var target: String? = null

//    var procedureFee: String? =null,
//    var underwritingPolicy: String? =null
)

data class Query2(
    var insuranceCompany: String? = null,
)

data class QueryResult (val list: List<QueryResultItem>)
data class  QueryResult2 (val list: List<QR2>)

data class QueryResultItem(
    var carModel: String?,
    var area: String?,
    var insuranceCompany: String?,
    var carOwner: String?,

    var procedureFee1: String?,
    var procedureFee2: String?,
    var request: String?
)


data class QR2(
    var insuranceCompany: String?,
    var producerFee:String?,
    var request: String?
)

fun fromCsv(line: List<String>): QueryResultItem {
    return QueryResultItem(
        carModel = line[0],
        area = line[1],
        insuranceCompany = line[2],
        carOwner = line[3],
        procedureFee1 = line[4],
        procedureFee2 = line[5],
        request = line[6]
    )

}
fun fromCsv2(line: List<String>): QR2 {
    return QR2(
        insuranceCompany = line[0],
        producerFee = line[1],
        request = line[2]
    )

}
