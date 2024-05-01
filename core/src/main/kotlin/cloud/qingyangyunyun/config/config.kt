package cloud.qingyangyunyun.config

import cloud.qingyangyunyun.ai.JsonData
import java.io.File

interface WithConfig<C> {
    var config: C
    val defaultConfig: C?
    fun setCon(config: C) {
        this.config = config
        this.useConfig(this.config)
        this.save()
    }

    fun getCon(): C {
        return this.config
    }

    fun save()
    fun load()
    fun useConfig(config: C)
}

abstract class JsonDataConfig<C : Any>(val path: File, val clazz: Class<C>) : WithConfig<C> {
    override var config: C = defaultConfig!!
    final override fun load() {
        val saveDefault = (defaultConfig != null)
        this.config = JsonData(path).load(defaultConfig!!, clazz, saveDefault)
    }

    override fun save() {
        JsonData(path).save(config, clazz)
    }

}