package cloud.qingyangyunyun.ai

import cloud.qingyangyunyun.ai.agent.IntentsDefine
import cloud.qingyangyunyun.ai.agent.Tone
import cloud.qingyangyunyun.ai.agent.UnderStood
import cloud.qingyangyunyun.ai.agent.car.carDefine
import cloud.qingyangyunyun.ai.agent.car.carTone
import cloud.qingyangyunyun.ai.cache.CacheHolder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackages = ["cloud.qingyangyunyun"])
class DemoApplication() {


    @Bean
    fun def(): IntentsDefine {
        return carDefine
    }

    @Bean
    fun tone(): Tone = carTone

    @Bean
    fun cacheHolder(): CacheHolder<*, *> {
        return CacheHolder<String, UnderStood>()
    }

    @Bean
    fun cacheHolders():Map<String, CacheHolder<*, *>>{
        return mapOf("understood" to cacheHolder())
    }


}


fun main(args: Array<String>) {
    runApplication<DemoApplication>()
}
