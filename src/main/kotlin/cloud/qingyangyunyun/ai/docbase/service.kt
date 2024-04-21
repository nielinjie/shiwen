package cloud.qingyangyunyun.ai.docbase

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.springframework.stereotype.Service
import xyz.nietongxue.common.base.Attrs
import xyz.nietongxue.docbase.*
import java.io.File

@Service
class DocbaseService(){
    val events = mutableListOf<DocChangeEvent>()

    val docbaseFile = File("./fileDocbase")
    val fileSystemImporter = FileSystemImporter(docbaseFile)
    val base = DefaultBase(DoNothingPersistence, listOf(object : DocListener {
        override fun onChanged(docChangeEvent: DocChangeEvent) {
            events.add(docChangeEvent)
        }
    }, fileSystemImporter, SegmentDerivingTrigger(fileSystemImporter)))

    fun listDocs():List<DocInfo>{
        return this.base.docs.map { DocInfo(it.id(),it.name) }
    }
    fun getDoc(id: String):DocObject{
        val doc = this.base.docs.find { it.id() == id } ?: throw IllegalArgumentException("doc not found")
        return DocObject(doc.id(),doc.name,doc.content,doc.attrs)
    }
}

@Serializable
data class DocInfo(val id: String, val name:String)
@Serializable
data class DocObject(val id: String,val name: String,val content: String,val attrs: Attrs<JsonElement>)
