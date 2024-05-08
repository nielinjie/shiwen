package cloud.qingyangyunyun.ai.embedding

import cloud.qingyangyunyun.ai.docbase.BaseIndexer
import cloud.qingyangyunyun.ai.docbase.IndexResult
import cloud.qingyangyunyun.ai.vector.VectorStore
import org.springframework.ai.document.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.docbase.Doc

@Component
class EmbeddingIndexer(@Autowired val vectorStore: VectorStore) : BaseIndexer() {

    fun Doc.document(): Document? {
        if (this.content.isEmpty()) return null
        return Document(this.id(), this.content, this.attrs)
    }

    override fun search(query: String): List<Id> {
        return vectorStore.similaritySearch(query).map { it.id }
    }

    override fun index(doc: Doc): IndexResult {
        //TODO check index existed.
        vectorStore.accept(listOfNotNull(doc.document()))
        return IndexResult.Success
    }

    override fun removeIndex(docId: Id) {
        vectorStore.delete(listOf(docId))

    }
    override fun flush() {
        vectorStore.save()
    }


}