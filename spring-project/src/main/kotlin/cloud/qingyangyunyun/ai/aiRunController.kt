package cloud.qingyangyunyun.ai

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
                runRequest.prompt, mapOf("input" to runRequest.input), runRequest.runner
            ), runRequest.runner
        )
    }

    @GetMapping("/api/runners")
    fun run(): List<String> {
        return service.getRunners()
    }
}

data class RunRequest(
    val runner: String,
    val input: String,
    val prompt: String
)

data class RunResponse(
    val result: String,
    val runner:String
)

