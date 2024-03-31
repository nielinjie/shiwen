package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.workshop.Workspace
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.stereotype.Component
import java.io.File

class JsonData(val path: File) {
    inline fun <reified T> load(default: T): T {
        val json = try {
            path.readText()
        } catch (e: Exception) {
            return default
        }

        return Json.decodeFromString(serializer<T>(), json)
    }

    inline fun <reified T> save(value: T) {
        val json = Json.encodeToString(serializer<T>(), value)
        path.writeText(json)
    }
}

