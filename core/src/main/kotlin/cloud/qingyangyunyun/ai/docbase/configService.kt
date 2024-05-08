package cloud.qingyangyunyun.ai.docbase

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ConfigService(@Autowired val service: DocbaseService) {
    fun filesConfig(): List<String> {
        return service.getCon().filePaths
    }
    fun filesConfig(config: List<String>) {
        service.setCon(service.getCon().copy(filePaths = config))
    }
}