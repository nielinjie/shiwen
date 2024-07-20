package cloud.qingyangyunyun.ai.function

import org.springframework.ai.model.function.FunctionCallbackWrapper


import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class FunctionService() {
    val callbacks = listOf(
        FunctionCallbackWrapper.builder(MockWeatherService()).withName("CurrentWeather")
            .withDescription("Get the weather in location").build()
    )

    fun callbacks(names: List<String>): List<FunctionCallbackWrapper<*, *>> {
        return callbacks.filter {
            it.name in names
        }

    }


}


class MockWeatherService : Function<MockWeatherService.Request?, MockWeatherService.Response?> {
    enum class Unit {
        C, F
    }

    @JvmRecord
    data class Request(val location: String, val unit: Unit)

    @JvmRecord
    data class Response(val temp: Double, val unit: Unit)

    override fun apply(request: Request?): Response {
        return Response(30.0, Unit.C)
    }
}

