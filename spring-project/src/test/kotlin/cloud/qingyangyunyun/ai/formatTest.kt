package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.chat.Intent
import cloud.qingyangyunyun.ai.chat.displayResult
import cloud.qingyangyunyun.custom.carInsurance.QueryResultItem
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.springframework.boot.test.context.SpringBootTest

class FormatTest(
) : StringSpec({
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
    "short format" {
        val intent = Intent.fromString("公司")
        val output = displayResult(result, intent)
        output.also {
            println(it)
            assertThat(it.lines(), hasSize(1))
        }
    }
    "long format" {
        val intent = Intent.fromString("费用")
        val output = displayResult(result, intent)
        output.also {
            println(it)
            assertThat(it.lines(), hasSize(4))
        }
    }
})