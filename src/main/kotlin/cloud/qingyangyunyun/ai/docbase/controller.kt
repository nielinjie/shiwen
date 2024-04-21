package cloud.qingyangyunyun.ai.docbase

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
class DocbaseController(
    @Autowired val service: DocbaseService
) {


    @GetMapping("/api/docbase/docs")
    fun getDocs(): List<DocInfo> {
        return service.listDocs()
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
}

data class BaseInfoResponse(val info: String)


