package cloud.qingyangyunyun.custom.carInsurance

import cloud.qingyangyunyun.ai.agent.IntentsDefine
import cloud.qingyangyunyun.ai.agent.Tone
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configure {
    @Bean
    fun define(): IntentsDefine {
        return carDefine
    }
    @Bean
    fun tone(): Tone {
        return carTone
    }
}