package cloud.qingyangyunyun.ai.docbase

import cloud.qingyangyunyun.ai.workshop.Paths
import cloud.qingyangyunyun.config.JsonDataConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import xyz.nietongxue.common.base.Attrs
import xyz.nietongxue.docbase.*
import java.io.File

@Serializable
data class DocbaseConfig(val filePaths: List<String>)

@Service
class DocbaseService(@Autowired val paths: Paths) : JsonDataConfig<DocbaseConfig>(
    paths.docbaseConfig, DocbaseConfig::class.java
) {
    lateinit var base: DefaultBase

    val events = mutableListOf<DocChangeEvent>()

    override val defaultConfig: DocbaseConfig?
        get() = DocbaseConfig(listOf("./fileDocbase"))

    override fun useConfig(config: DocbaseConfig) {
        val docbaseFile = config.filePaths.first().let { File(it) }
        val fileSystemImporter = FileSystemImporter(docbaseFile)
        this.base = DefaultBase(DoNothingPersistence,
            listOf(
//                object : DocListener {
//            override fun onChanged(docChangeEvent: DocChangeEvent) {
//                events.add(docChangeEvent)
//            }
//        },
                fileSystemImporter, SegmentDerivingTrigger(fileSystemImporter)))

    }



    fun listDocs(): List<DocInfo> {
        return this.base.docs.map { DocInfo(it.id(), it.name) }
    }

    fun getDoc(id: String): DocObject {
        val doc = this.base.docs.find { it.id() == id } ?: throw IllegalArgumentException("doc not found")
        return DocObject(doc.id(), doc.name, doc.content, doc.attrs)
    }
}

@Serializable
data class DocInfo(val id: String, val name: String)

@Serializable
data class DocObject(val id: String, val name: String, val content: String, val attrs: Attrs<JsonElement>)
