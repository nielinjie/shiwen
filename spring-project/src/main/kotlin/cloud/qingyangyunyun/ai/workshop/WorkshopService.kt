package cloud.qingyangyunyun.ai.workshop

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import java.io.File

@Component
class WorkshopService() {
    val savePath = File("./workshop.json")

    fun save(workspace: Workspace) {
        val json = Json.encodeToString(workspace)
        savePath.writeText(json)
    }

    fun load(): Workspace {
        val json = try {
            savePath.readText()
        } catch (e: Exception) {
            return Workspace("", "", emptyList())
        }

        return Json.decodeFromString(Workspace.serializer(), json)
    }
}