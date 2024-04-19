package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.ai.log.LogStore
import cloud.qingyangyunyun.custom.carInsurance.Data
import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


class Session(
    @Autowired val data: Data,
    @Autowired var logStore: LogStore? = null
) {
    var state: State = State.Start
    fun logState() {
        logStore?.add("state: \n$state")
    }

    fun logReplay(re: Replay) {
        logStore?.add("replay: \n$re")
    }

    fun logUnderstanding(understanding: Understanding) {
        logStore?.add("understanding: \n$understanding")
    }

    fun input(understanding: Understanding): Replay {
        logState()
        val o = this.state.byInput(understanding.also { logUnderstanding(it) })
        this.state = o.first
        logState()
        return o.second.also {
            logReplay(it)
        }
    }

    fun restart() {
        this.state = State.Start
    }

    fun result(): QueryResult {
        return when (this.state) {
            is State.Chatting -> (this.state as State.Chatting).queryResult(data)
            else -> error("not support state")
        }
    }
}

data class Intent(val value: String) {
    companion object {
        fun fromString(string: String): Intent {
            return Intent(string)
        }
    }
}
