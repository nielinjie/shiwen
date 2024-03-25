package cloud.qingyangyunyun.ai.workshop

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import java.io.File

@Component
class WorkshopService() {
    val workshopBasePath = File("./workshop")
    val savePath = File(workshopBasePath, "./state.json")
    val configPath = File(workshopBasePath, "./clients.json")
    fun save(workspace: Workspace) {
        val json = Json.encodeToString(workspace)
        savePath.writeText(json)
    }

    fun load(): Workspace {
        val json = try {
            savePath.readText()
        } catch (e: Exception) {
            return Workspace(emptyList(), emptyList(), emptyList())
        }

        return Json.decodeFromString(Workspace.serializer(), json)
    }
}