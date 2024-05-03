package cloud.qingyangyunyun.ai.docbase

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.nietongxue.common.base.Id
import xyz.nietongxue.docbase.SegmentMethod
import xyz.nietongxue.docbase.filetypes.FileType
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
class DocbaseController(
    @Autowired val service: DocbaseService,
    @Autowired val indexers: Map<String, Indexer>,
    @Autowired val segmentMethods: Map<String, SegmentMethod>,
    @Autowired val fileTypes: Map<String, FileType>
) {

    @GetMapping("/api/docbase/indexers")
    fun getIndexers(): List<String> {
        return indexers.keys.toList() //TODO "Embedding, Tokenizer, etc."
    }

    @GetMapping("/api/docbase/segmentMethods")
    fun getSegments(): List<String> {
        return segmentMethods.keys.toList()
    }

    @GetMapping("/api/docbase/fileTypes")
    fun getFileTypes(): List<String> {
        return fileTypes.keys.toList()
    }


    @GetMapping("/api/docbase/docs")
    fun getDocs(): List<DocInfo> {
        return service.listDocs()
    }

    @GetMapping("/api/docbase/referringDocs")
    fun getReferringDocs(): List<DocInfo> {
        return service.listReferringDocs()
    }

    @GetMapping("/api/docbase/derivedDocs/referring/{id}")
    fun getDerivedDocs(@PathVariable id: String): List<DocInfo> {
        return service.listDerivedDocs(decodeUrlSafe(decodeBase64(id)))
    }

    @GetMapping("/api/docbase/docs/{id}")
    fun getDoc(@PathVariable id: String): DocObject {
        return service.getDoc(decodeUrlSafe(decodeBase64(id)))
//        return service.getDoc((id))
    }

    //TODO rest 规范需要遵守，放到queryString？
    @PostMapping("/api/docbase/docs/ids")
    fun getDocs(@RequestBody ids: List<String>): List<DocObject> {
        return ids.map { service.getDoc(it) }

    }

    fun decodeUrlSafe(input: String): String {
        return URLDecoder.decode(input, StandardCharsets.UTF_8.toString())
    }


    fun decodeBase64(input: String): String {
        val decodedBytes = Base64.getDecoder().decode(input)
        return String(decodedBytes, Charsets.UTF_8)
    }

    @GetMapping("/api/docbase/info")
    fun getBaseInfo(): BaseInfoResponse {
        return BaseInfoResponse(
            BaseInfo(
                service.sources(), indexers.keys.toList(), segmentMethods.keys.toList(), fileTypes.keys.toList()
            )
        )
    }

    @PostMapping("/api/docbase/search")
    fun search(@RequestBody searching: Searching): List<DocInfo> {
        return service.search(searching)
    }

}

@Serializable
data class BaseInfo(
    val sources: List<String>, val indexers: List<String>, val segmentMethods: List<String>, val fileTypes: List<String>
)

@Serializable
data class BaseInfoResponse(val info: BaseInfo)

