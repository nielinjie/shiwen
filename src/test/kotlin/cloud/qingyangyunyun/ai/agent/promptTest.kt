package cloud.qingyangyunyun.ai.agent

import cloud.qingyangyunyun.ai.agent.car.carDefine
import io.kotest.core.spec.style.StringSpec

class PromptTest : StringSpec({
    "prompts" {
        val prompt = promptForIntents(carDefine.intentDefs)
        println(prompt)
    }
})