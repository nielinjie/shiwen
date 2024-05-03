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
import xyz.nietongxue.docbase.*
import xyz.nietongxue.docbase.filetypes.FileType
import xyz.nietongxue.docbase.filetypes.Txt
import java.io.File


@Serializable
data class IndexedStatus(val id: String, val indexerNames: List<String>)

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

    @Bean
    fun segmentMethods(): Map<String, SegmentMethod> {
        return mapOf("Whole" to SegmentMethod.WholeSegment, "Line" to SegmentMethod.LineSegment)
    }

    @Bean
    fun fileTypes(): Map<String, FileType> {
        return mapOf("Txt" to Txt())
    }
}

@Service
class DocbaseService(
    @Autowired val paths: Paths,
    @Autowired val indexers: Map<String, Indexer>,
    @Autowired val segmentMethods: Map<String, SegmentMethod>,
    @Autowired val fileTypes: Map<String, FileType>
) :
    JsonDataConfig<DocbaseConfig>(
        paths.docbaseConfig, DocbaseConfig::class.java
    ) {
    lateinit var base: DefaultBase
    lateinit var fileSystemImporter: FileSystemImporter

    val events = mutableListOf<DocChangeEvent>()

    override val defaultConfig: DocbaseConfig?
        get() = DocbaseConfig(listOf("./fileDocbase"))

    init {
        load()
        useConfig(this.config)
    }

    override fun useConfig(config: DocbaseConfig) {
        val docbaseFile = config.filePaths.first().let { File(it) }
        fileSystemImporter = FileSystemImporter(docbaseFile)

        this.base = DefaultBase(
            DoNothingPersistence, listOf(
//                object : DocListener {
//            override fun onChanged(docChangeEvent: DocChangeEvent) {
//                events.add(docChangeEvent)
//            }
//        },
                fileSystemImporter,
                SegmentDerivingTrigger(
                    fileSystemImporter,
                    this.segmentMethods.values.toList(),
                    this.fileTypes.values.toList()
                )
            ) + indexers.values
        )

    }

    fun sources(): List<String> {
        return listOf("fileSystemSource(${this.fileSystemImporter.basePath})")
    }

    fun listDocs(): List<DocInfo> {
        return this.base.docs.map { docToInfo(it) }
    }

    fun getDoc(id: String): DocObject {
        val doc = this.base.get(id)
        return DocObject(doc.id(), doc.name, doc.content, doc.attrs)
    }

    fun listReferringDocs(): List<DocInfo> {
        return this.base.docs.filterIsInstance<ReferringDoc>().map { docToInfo(it) }
    }

    fun listDerivedDocs(referringDocId: String): List<DocInfo> {
        val referringDoc = this.base.docs.find { it.id() == referringDocId } as? ReferringDoc
            ?: throw IllegalArgumentException("doc not found")
        return getDerivedDocs(referringDoc, this.base).map { docToInfo(it) }

    }

    fun search(searching: Searching): List<DocInfo> {

        val indexer = if (searching.indexer.isEmpty()) indexers.values.first() else this.indexers.get(searching.indexer)
            ?: error("indexer not found")
        return indexer.search(searching.queryString).map { idToDocInfo(it) }
    }

    fun idToDocInfo(id: String): DocInfo {
        val doc = this.base.get(id)
        return DocInfo(
            id, doc.name, ((doc as? DerivedDoc)?.derived?.meta ?: emptyMap()) +
                    mapOf("indexers" to indexedStatus(id).indexerNames.joinToString(","))
        )
    }

    fun docToInfo(doc: Doc): DocInfo {
        return DocInfo(
            doc.id(),
            doc.name,
            ((doc as? DerivedDoc)?.derived?.meta
                ?: emptyMap()) +
                    mapOf("indexers" to indexedStatus(doc.id()).indexerNames.joinToString(","))
        )
    }

    fun indexedStatus(id: String): IndexedStatus { //TODO 可能有性能问题
        return this.base.get(id).let { doc ->
            val indexers = this.indexers.filter { it.value.indexed(id) }.map { it.key }
            IndexedStatus(id, indexers)
        }
    }
}

@Serializable
data class DocInfo(val id: String, val name: String, val meta: Map<String, String>)

@Serializable
data class DocObject(val id: String, val name: String, val content: String, val attrs: Attrs<JsonElement>)
