package cloud.qingyangyunyun.ai.workshop

import org.springframework.stereotype.Component
import java.io.File

@Component
class Paths {
    val workshopBasePath = File("./workshop")
    val statePath = File(workshopBasePath, "./state.json")
    val configPath = File(workshopBasePath, "./clients.json")
    val localPrompts = File(workshopBasePath, "./prompts.json")
}