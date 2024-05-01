package cloud.qingyangyunyun.ai.docbase

import xyz.nietongxue.common.base.Change
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.docbase.*

interface Indexer : DocListener, BaseListener {
    override fun onOpen(base: Base) {
        (base as? DefaultBase)?.also {
            it.docs.forEach { doc -> index(doc) }
        }
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

    fun search(query: String): List<Id>
    fun index(doc: Doc) //check and index, if unchanged, do nothing
    fun removeIndex(docId: Id)
    fun indexed(docId:Id): Boolean
}

class SimpleIndexer : Indexer {
    private val index: MutableList<Pair<String, Id>> = mutableListOf()

    override fun search(query: String): List<Id> {
        return index.filter { it.first.contains(query) }.map { it.second }
    }

    override fun index(doc: Doc) {
        index.removeIf { it.second == doc.id() }
        index.add(doc.content to doc.id())
    }

    override fun removeIndex(docId: Id) {
        index.removeIf { it.second == docId }
    }

    override fun indexed(docId: Id): Boolean {
        return index.any { it.second == docId }
    }


}