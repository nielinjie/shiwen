package cloud.qingyangyunyun.ai.docbase

import cloud.qingyangyunyun.ai.Paths
import cloud.qingyangyunyun.config.JsonDataConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import xyz.nietongxue.common.base.Attrs
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.docbase.*
import java.io.File


@Serializable
data class IndexedStatus(val indexed: Map<Id, List<String>>)

@Serializable
data class Searching(val queryString: String, val indexer: String)

@Serializable
data class DocbaseConfig(val filePaths: List<String>)

@Configuration
class DocbaseConfiguration {
    @Bean
    fun indexers(): Map<String, Indexer> {
        return mapOf("Simple" to SimpleIndexer())
    }
}

@Service
class DocbaseService(
    @Autowired val paths: Paths,
    @Autowired val indexers: Map<String, Indexer>
) :
    JsonDataConfig<DocbaseConfig>(
        paths.docbaseConfig, DocbaseConfig::class.java
    ) {
    lateinit var base: DefaultBase

    val events = mutableListOf<DocChangeEvent>()

    override val defaultConfig: DocbaseConfig?
        get() = DocbaseConfig(listOf("./fileDocbase"))

    init {
        load()
        useConfig(this.config)
    }

    override fun useConfig(config: DocbaseConfig) {
        val docbaseFile = config.filePaths.first().let { File(it) }
        val fileSystemImporter = FileSystemImporter(docbaseFile)

        this.base = DefaultBase(
            DoNothingPersistence, listOf(
//                object : DocListener {
//            override fun onChanged(docChangeEvent: DocChangeEvent) {
//                events.add(docChangeEvent)
//            }
//        },
                fileSystemImporter, SegmentDerivingTrigger(fileSystemImporter)
            ) + indexers.values
        )

    }


    fun listDocs(): List<DocInfo> {
        return this.base.docs.map { DocInfo(it.id(), it.name) }
    }

    fun getDoc(id: String): DocObject {
        val doc = this.base.docs.find { it.id() == id } ?: throw IllegalArgumentException("doc not found")
        return DocObject(doc.id(), doc.name, doc.content, doc.attrs)
    }

    fun listReferringDocs(): List<DocInfo> {
        return this.base.docs.filterIsInstance<ReferringDoc>().map { DocInfo(it.id(), it.name) }
    }

    fun listDerivedDocs(referringDocId: String): List<DocInfo> {
        val referringDoc = this.base.docs.find { it.id() == referringDocId } as? ReferringDoc
            ?: throw IllegalArgumentException("doc not found")
        return getDerivedDocs(referringDoc, this.base).map { DocInfo(it.id(), it.name) }

    }

    fun search(searching: Searching): List<DocInfo> {

        val indexer = if (searching.indexer.isEmpty()) indexers.values.first() else this.indexers.get(searching.indexer)
            ?: error("indexer not found")
        return indexer.search(searching.queryString).map { DocInfo(it, this.base.get(it).name) }
    }

    fun indexedStatus(): IndexedStatus {
        return this.base.docs.map { doc ->
            val id = doc.id()
            doc.id() to this.indexers.filter { it.value.indexed(id) }.map { it.key }
        }.let { IndexedStatus(it.toMap()) }
    }
}

@Serializable
data class DocInfo(val id: String, val name: String)

@Serializable
data class DocObject(val id: String, val name: String, val content: String, val attrs: Attrs<JsonElement>)
