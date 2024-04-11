package cloud.qingyangyunyun.custom.carInsurance

import cloud.qingyangyunyun.ai.chat.Intent
import org.springframework.stereotype.Component


/*
（车型、车主、保险公司、手续费、承保政策）
CarModel,CarOwner, InsuranceCompany, ProcedureFee, UnderwritingPolicy
 */
@Component
class Data {
    val companies: List<String> = javaClass.getResource("" +
            "/cloud/qingyangyunyun/custom/carInsurance/companies.txt")!!.readText().lines().map {
        it.trim()
    }.distinct()
    fun query(query: Query): QueryResult {
        return listOf(
            QueryResultItem(
                carModel = "奥迪",
                carOwner = "张三",
                insuranceCompany = "平安",
                procedureFee = "1000",
                underwritingPolicy = "交强险有各种规定"
            )
        )
    }
}

data class Query(
    var carModel: String? =null,
    var carOwner: String? = null,
    var area:String?=null,
    var insuranceCompany: String? =null,
//    var procedureFee: String? =null,
//    var underwritingPolicy: String? =null
)

typealias QueryResult = List<QueryResultItem>
data class QueryResultItem(
    var carModel: String?,
    var carOwner: String?,
    var insuranceCompany: String?,
    var procedureFee: String?,
    var underwritingPolicy: String?
)
