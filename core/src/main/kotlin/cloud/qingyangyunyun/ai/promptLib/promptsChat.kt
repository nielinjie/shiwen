package cloud.qingyangyunyun.ai.promptLib

import cloud.qingyangyunyun.ai.JsonData
import cloud.qingyangyunyun.ai.workshop.Paths
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.id
import java.io.IOException


//https://prompts.chat
@Component
class PromptsChatSeed(@Autowired paths: Paths) : Source {
    val prompts = mutableListOf<Prompt>()
    val data = JsonData(paths.promptsChat)

    init {
        prompts.addAll(data.load<List<Prompt>>(emptyList()))
    }

    override fun prompts(): List<Prompt> {
        return prompts
    }

    override fun info(): SourceInfo {
        return SourceInfo(
            "Prompts Chat", "A prompt seed scrap from https://prompts.chat", "prompts-chat", "seed"
        )
    }

}


//fun main(){
//    getDoc()
//}

//fun getDoc() {
//    fun getDocument(url: String?): HtmlPage? {
//        var page: HtmlPage? = null
//        try {
//            WebClient().use { webClient ->
//                webClient.options.isCssEnabled = false
//                webClient.options.isJavaScriptEnabled = false
//                page = webClient.getPage(url)
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return page
//    }
//
//    val page = getDocument("https://prompts.chat")
//    page!!.let {
//        println("got from ${it.titleText}")
//    }
//    val elements: List<HtmlElement> = page.getByXPath("//h1[@id='prompts']/following-sibling::h2")
//    val prompts = elements.drop(1).mapNotNull {
//        val title = it.textContent
//        val content = it.getFirstByXPath<HtmlElement>("following-sibling::blockquote[1]")?.textContent
//        content?.let {
//            Prompt(it.trim(), title, id(), "prompts-chat", emptyMap())
//        }
//    }
//    JsonData(Paths().promptsChat).save(prompts)
//
//}






