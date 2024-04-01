package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.workshop.Workspace
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.stereotype.Component
import java.io.File

class JsonData(val path: File) {
    inline fun <reified T> load(default: T, saveDefault: Boolean = false): T {
        if (!path.exists()) {
            if (saveDefault) save(default)
            return default

        }
        val json = path.readText()
        return Json.decodeFromString(serializer<T>(), json)
    }

    inline fun <reified T> save(value: T) {
        val json = Json.encodeToString(serializer<T>(), value)
        path.parentFile.mkdirs()
        path.writeText(json)
    }
}

