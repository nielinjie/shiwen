package cloud.qingyangyunyun.ai.cache

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


class CacheHolder<K, V> {
    val map = mutableMapOf<K, V>()
    fun reset() {
        this.map.clear()
    }

    var byPass = false
    fun getOrPut(key: K, defaultValue: () -> V): V {
        if (byPass) {
            return defaultValue()
        }
        return this.map.getOrPut(key) { defaultValue() }
    }
}


@RestController
class CacheController(
    @Autowired
    val cacheHolders: Map<String, CacheHolder<*, *>>,
) {
    @PostMapping("/api/caches/{name}/reset")
    fun resetCache(@PathVariable name: String) {
        cacheHolders[name]?.reset()
    }

    @PostMapping("/api/caches/{name}/bypass")
    fun bypassCache(@PathVariable name: String) {
        cacheHolders[name]?.byPass = true
    }

    @PostMapping("/api/caches/{name}/unbypass")
    fun unbypassCache(@PathVariable name: String) {
        cacheHolders[name]?.byPass = false
    }
}