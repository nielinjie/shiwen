package cloud.qingyangyunyun.ai

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
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

    fun <T : Any> load(default: T, clazz: Class<T>, saveDefault: Boolean = false): T {
        if (!path.exists()) {
            if (saveDefault) save(default, clazz)
            return default

        }
        val json = path.readText()
        return Json.decodeFromString(serializer(clazz), json) as T
    }

    fun <T : Any> save(value: T, clazz: Class<T>) {
        val json = Json.encodeToString(serializer(clazz), value)
        path.parentFile.mkdirs()
        path.writeText(json)
    }

    inline fun <reified T> save(value: T) {
        val json = Json.encodeToString(serializer<T>(), value)
        path.parentFile.mkdirs()
        path.writeText(json)
    }
}

