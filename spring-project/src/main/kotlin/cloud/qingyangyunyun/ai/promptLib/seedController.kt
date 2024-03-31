package cloud.qingyangyunyun.ai.promptLib

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class SeedController(
    @Autowired val seedService: SeedService
) {


    @GetMapping("/api/sources")
    fun seeds(): List<SourceInfo> {
        return seedService.getSeeds().map { it.info() }
    }

    @GetMapping("/api/prompts")
    fun search(@RequestParam q: String): List<Prompt> {

        return seedService.search(q)
    }

    @PostMapping("/api/prompts")
    fun savePrompt( @RequestBody prompt: PromptRequest) {
        seedService.savePrompt(prompt)
    }

}
