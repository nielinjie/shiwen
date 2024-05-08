package cloud.qingyangyunyun.ai.vector

import cloud.qingyangyunyun.ai.Paths
import cloud.qingyangyunyun.ai.clients.ClientsService
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class VectorStore(
    @Autowired val clientsService: ClientsService,
    @Autowired val paths: Paths
) : VectorStore {
    lateinit var simple: SimpleVectorStore

    init {
        simple = SimpleVectorStore(clientsService.getDefaultEmbeddingClient())
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
        simple.save(paths.vectorStoreDataPath)
    }

}
