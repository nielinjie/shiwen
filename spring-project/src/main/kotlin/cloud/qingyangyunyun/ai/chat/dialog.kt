package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


typealias Input = String
typealias Output = Replay?


fun <T, U> Result<T>.flatmap(f: (T) -> Result<U>): Result<U> {
    return when {
        this.isSuccess -> f(this.getOrNull()!!)
        this.isFailure -> return Result.failure(this.exceptionOrNull()!!)
        else -> throw IllegalStateException("Result is neither success nor failure")
    }
}

//@Component
class Dialog(
    @Autowired val session: Session, @Autowired val data: Data, @Autowired val nlu: NLU,
    @Autowired val nlg: NLG
) {
    fun run(input: String): String {
        val replay: Replay = nlu.parse(input).let {
            session.input(it)
        }
        return nlg.output(replay, input = input, session = session)

    }
}