package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.chat.Intent
import cloud.qingyangyunyun.ai.chat.NLG
import cloud.qingyangyunyun.custom.carInsurance.QueryResultItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FormatTest{
    @Autowired
    var nlg: NLG? = null

    val result = listOf(
        QueryResultItem(
            carModel = "新能源网约车",
            area = "成都",
            carOwner = "个人",
            insuranceCompany = "人保",
            procedureFee1 = "1000",
            procedureFee2 = "2000",
            underwritingPolicy = "保险公司A"
        ),
        QueryResultItem(
            carModel = "新能源网约车",
            area = "成都",
            carOwner = "个人",
            insuranceCompany = "平安",
            procedureFee1 = "1000",
            procedureFee2 = "2000",
            underwritingPolicy = "保险公司A"
        ),
    )
    @Test
    fun testFormat(){
        val intent = Intent.fromString("公司")
        val output = nlg?.result(result,intent)
        output!!.also {
            println(it)
            assertThat(it.lines(),hasSize(1))
        }
    }
    @Test
    fun testLongFormat(){
        val intent = Intent.fromString("费用")
        val output = nlg?.result(result,intent)
        output!!.also {
            println(it)
            assertThat(it.lines(),hasSize(4))
        }
    }
}