package cloud.qingyangyunyun.ai.app




import cloud.qingyangyunyun.ai.agent.*
import cloud.qingyangyunyun.ai.cache.CacheHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.awt.Desktop
import java.net.URI


@SpringBootApplication(scanBasePackages = ["cloud.qingyangyunyun"])
class DemoApplicationCustom() {


//    @Bean
//    fun intents(): IntentsDefine {
//        return object : IntentsDefine {
//            override val intentDefs: List<IntentDef>
//                get() = emptyList()
//
//            override fun run(holding: IntentHolding, currentState: State): State {
//                return currentState
//            }
//        }
//    }
//
//    @Bean
//    fun tone(): Tone {
//        return object : Tone {
//            override fun help(): Output {
//                return ("mock help").plain()
//            }
//        }
//    }

    @Bean
    fun cacheHolder(): CacheHolder<*, *> {
        return CacheHolder<String, UnderStood>()
    }

    @Bean
    fun cacheHolders(): Map<String, CacheHolder<*, *>> {
        return mapOf("understood" to cacheHolder())
    }


}

@Component
class StartupApplicationListener : ApplicationListener<ApplicationReadyEvent> {
    @Value("\${server.port}")
    var port:String?=null
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val url = "http://localhost:"+(port?:"8080")+"/"
        println("Application started. Click to open in browser: $url")

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(url))
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplicationCustom>()
}
