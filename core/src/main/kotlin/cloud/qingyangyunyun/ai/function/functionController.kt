package cloud.qingyangyunyun.ai.function

import kotlinx.serialization.Serializable
import org.springframework.context.annotation.Description
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FunctionController {
    @GetMapping("/api/functions")
    fun functions(): List<Fun> {
        return listOf(Fun("CurrentWeather", "Get the weather in location"))
    }
}

@Serializable
data class Fun(val name: String, val description: String)