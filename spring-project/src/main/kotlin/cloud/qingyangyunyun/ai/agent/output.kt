package cloud.qingyangyunyun.ai.agent

import kotlinx.serialization.Serializable

@Serializable
data class Output(val type: String, val body: String) {
    operator fun plus(output: Output): Output {
        return when (this.type to output.type) {
            "plainText" to "plainText" -> plainText(this.body n output.body)
            "plainText" to "markdown" -> markdown((this.body) nn output.body)
            "markdown" to "plainText" -> markdown(this.body n output.body)
            "markdown" to "markdown" -> markdown(this.body nn output.body)
            else -> plainText(this.body + output.body)
        }

    }

}

infix fun String.n(b: String): String {
    return this + "\n" + b
}

infix fun String.nn(b: String): String {
    return this + "\n\n" + b
}

val List<String>.n: String
    get() {
        return this.joinToString("\n")
    }
val List<String>.nn: String
    get() {
        return this.joinToString("\n\n")
    }


fun plainText(text: String): Output {
    return Output("plainText", text)
}

fun markdown(text: String): Output {
    return Output("markdown", text)
}

fun String.plain() = plainText(this)


