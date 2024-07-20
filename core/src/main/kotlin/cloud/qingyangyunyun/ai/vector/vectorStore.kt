package cloud.qingyangyunyun.ai.vector

import cloud.qingyangyunyun.ai.Paths
import cloud.qingyangyunyun.ai.clients.ClientsService
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.Id
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LocalVectorStore(val embeddingClient: EmbeddingClient) : SimpleVectorStore(embeddingClient) {
    fun getStore(): MutableMap<String, Document>? {
        return store
    }

}


@Component
class VectorStore(
    @Autowired val clientsService: ClientsService,
    @Autowired val paths: Paths
) : VectorStore {
    lateinit var simple: LocalVectorStore

    init {
        simple = LocalVectorStore(clientsService.getDefaultEmbeddingClient())
    }

    override fun add(documents: MutableList<Document>?) {
        simple.add(documents)
    }

    override fun delete(idList: List<String>?): Optional<Boolean> {
        return simple.delete(idList)
    }

    override fun similaritySearch(request: SearchRequest?): List<Document> {
        return simple.similaritySearch(request)
    }

    fun load() {
        simple.load(paths.vectorStoreDataPath)
    }

    fun save() {
        paths.vectorStoreDataPath.prepareParent()
        simple.save(paths.vectorStoreDataPath)
    }

    fun existed(id: Id): Boolean {
        return this.simple.getStore()?.contains(id) ?: false
    }

}

private fun File.prepareParent() {
    this.parentFile.mkdirs()
}
