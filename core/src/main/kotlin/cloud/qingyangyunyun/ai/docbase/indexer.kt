package cloud.qingyangyunyun.ai.docbase

import xyz.nietongxue.common.base.Change
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.docbase.*


interface Indexer {
    fun search(query: String): List<Id>
    fun index(doc: Doc): IndexResult//check and index, if unchanged, do nothing
    fun removeIndex(docId: Id)
    fun flush() {}
}

data class DocIndexResult(
    val id: Id,
    var result: IndexResult
)

interface IndexResult {
    data object Success : IndexResult
    data object Fail : IndexResult
    data object NotTried : IndexResult
    data object NotNeed : IndexResult
}

abstract class BaseIndexer : Indexer {
    lateinit var base: DefaultBase
    fun indexAll() {
        this.base.docs.filter { this.docFilter(it) }.forEach {
            this.results.add(DocIndexResult(it.id(), IndexResult.NotTried))
        }
        //TODO dispatch this to background thread?
        this.results.filter { it.result == IndexResult.NotTried }.chunked(5).forEach {
            it.forEach {
                val result = index(base.get(it.id))
                it.result = result
            }
            flush()
        }

    }

    fun docFilter(doc: Doc): Boolean {
        return doc.content.isNotEmpty()
    }

    val results: MutableList<DocIndexResult> = mutableListOf()
}


abstract class AutoIndexer : BaseIndexer(), DocListener, BaseListener {
    override fun onOpen(base: Base) {
        this.base = base as DefaultBase
//        require(this.base == base)
        indexAll()
    }

    override fun onChanged(docChangeEvent: DocChangeEvent) {
        when (docChangeEvent.change) {

            Change.Added -> index(docChangeEvent.doc)
            Change.Changed -> {
                removeIndex(docChangeEvent.doc.id())
                index(docChangeEvent.doc)
            }

            Change.Removed -> removeIndex(docChangeEvent.doc.id())
        }
    }
}

class SimpleIndexer : AutoIndexer() {
    private val index: MutableList<Pair<String, Id>> = mutableListOf()

    override fun search(query: String): List<Id> {
        return index.filter { it.first.contains(query) }.map { it.second }
    }

    override fun index(doc: Doc): IndexResult {
        index.removeIf { it.second == doc.id() }
        index.add(doc.content to doc.id())
        return IndexResult.Success
    }

    override fun removeIndex(docId: Id) {
        index.removeIf { it.second == docId }
    }
}