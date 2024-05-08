package cloud.qingyangyunyun.ai

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class Paths(
    @Value("\${fileBase:}")
    val fileBase: String?
) {


    val workshopBasePath: File
        get() =
            File(
                (if (fileBase.isNullOrEmpty())
                    System.getProperty("user.dir")
                else fileBase), "./.shiwen"
            ).absoluteFile

    val statePath: File
        get() = File(workshopBasePath, "./state.json")
    val configPath: File
        get() = File(workshopBasePath, "./clients.json")
    val localPrompts: File
        get() = File(workshopBasePath, "./prompts.json")
    val promptsChat: File
        get() = File(workshopBasePath, "./seeds/prompts-chat.json")
    val docbaseConfig: File
        get() = File(workshopBasePath, "./docbaseConfig.json")
    val vectorStoreDataPath: File
        get() = File(workshopBasePath, "./vectorStore/simpleVectorStore.json")
}