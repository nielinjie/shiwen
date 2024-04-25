package cloud.qingyangyunyun.website

import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MarkdownController() {
    val path = "/markdowns"

    @GetMapping("/api/markdown/{name}")
    fun getMarkdown(@PathVariable name: String): BasicDoc {
        return (this.javaClass.getResource("/markdowns/$name")?.readText() )?.let {
            BasicDoc(name, it)
        } ?: error("not found")
    }
}

@Serializable
data class BasicDoc(
    val name: String,
    val content: String
)