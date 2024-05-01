package cloud.qingyangyunyun.ai.docbase

import kotlinx.serialization.Serializable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.nietongxue.common.base.Id
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
class DocbaseController(
    @Autowired val service: DocbaseService,
    @Autowired val indexers: Map<String, Indexer>
) {

    @GetMapping("/api/docbase/indexers")
    fun getIndexers(): List<String> {
        return indexers.keys.toList() //TODO "Embedding, Tokenizer, etc."
    }

    @GetMapping("/api/docbase/indexedStatus")
    fun getIndexedStatus(): IndexedStatus {
        return service.indexedStatus()
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

    fun decodeUrlSafe(input: String): String {
        return URLDecoder.decode(input, StandardCharsets.UTF_8.toString())
    }


    fun decodeBase64(input: String): String {
        val decodedBytes = Base64.getDecoder().decode(input)
        return String(decodedBytes, Charsets.UTF_8)
    }

    @GetMapping("/api/docbase/info")
    fun getBaseInfo(): BaseInfoResponse {
        return BaseInfoResponse("docbase")
    }

    @PostMapping("/api/docbase/search")
    fun search(@RequestBody searching: Searching): List<DocInfo> {
        return service.search(searching)
    }

}

@Serializable
data class BaseInfoResponse(val info: String)

