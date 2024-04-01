package cloud.qingyangyunyun.ai.promptLib

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class SeedController(
    @Autowired val sourceService: SourceService
) {


    @GetMapping("/api/sources")
    fun seeds(): List<SourceInfo> {
        return sourceService.getSources().map { it.info() }
    }

    @GetMapping("/api/prompts")
    fun search(@RequestParam q: String): List<Prompt> {

        return sourceService.search(q)
    }

    @PostMapping("/api/prompts")
    fun savePrompt( @RequestBody prompt: PromptRequest) {
        sourceService.savePrompt(prompt)
    }

}
