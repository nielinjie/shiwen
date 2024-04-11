package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


typealias Input = String
typealias Output = Request?

sealed class CommException(message: String) : Exception(message) {
    class NoQueryException : CommException("No query found")
    class CannotUnderstandException(val input: String) : CommException("Cannot understand the input")
}

fun <T, U> Result<T>.flatmap(f: (T) -> Result<U>): Result<U> {
    return when {
        this.isSuccess -> f(this.getOrNull()!!)
        this.isFailure -> return Result.failure(this.exceptionOrNull()!!)
        else -> throw IllegalStateException("Result is neither success nor failure")
    }
}

@Component
class Dialog(
    @Autowired val session: Session, @Autowired val data: Data, @Autowired val nlu: NLU,
    @Autowired val nlg: NLG
) {
    fun run(input: String): String {
        val request: Request = nlu.parse(input).let {
            session.input(it)
        }
        return nlg.output(request, input = input, session = session)

    }
}