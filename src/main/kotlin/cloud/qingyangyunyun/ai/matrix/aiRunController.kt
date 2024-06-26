package cloud.qingyangyunyun.ai.matrix

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AiRunController(@Autowired val service: Service) {
    @PostMapping("/api/run")
    fun run(@RequestBody runRequest: RunRequest): RunResponse {
        return RunResponse(
            service.call(
                runRequest.prompt, mapOf("input" to runRequest.input), runRequest.client
            ), runRequest.client
        )
    }


}

data class RunRequest(
    val client: String,
    val input: String,
    val prompt: String
)

data class RunResponse(
    val result: String,
    val client:String
)

