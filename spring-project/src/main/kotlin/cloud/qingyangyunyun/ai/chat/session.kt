package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.Data
import cloud.qingyangyunyun.custom.carInsurance.Query
import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

sealed interface State {
    fun byInput(understanding: Understanding): Pair<State, Request>

    object Start : State {
        override fun byInput(understanding: Understanding): Pair<State, Request> {
            return when (understanding) {
                is Understanding.Json -> {
                    val result = Chatting.fromJson(understanding)
                    result.fold(
                        onSuccess = {
                            it to when (it.intent) {
                                null -> Request.IntentRequest
                                else -> Request.Result
                            }
                        },
                        onFailure = {
                            Pair(Start, Request.Failed(it.message ?: ""))
                        }
                    )
                }

                is Understanding.NothingFind -> {
                    Start to Request.QueryRequest(listOf())
                }

                is Understanding.Failed -> {
                    Pair(Start, Request.Failed(understanding.message ?: ""))
                }
            }
        }
    }

    class Chatting(
        var query: Query? = null,
        var intent: Intent? = null
    ) : State {
        companion object {
            fun fromJson(ujson: Understanding.Json): Result<Chatting> {
                val re = Chatting()
                val json = ujson.jsonElement
                return re.dumpBy(json)
            }
        }

        fun dumpBy(json: JsonElement): Result<Chatting> {
            return this.runCatching {
                json.jsonObject.get("carModel")?.also { ensureQuery().carModel = it.jsonPrimitive.content }
                json.jsonObject.get("carOwner")?.also { ensureQuery().carOwner = it.jsonPrimitive.content }
                json.jsonObject.get("insuranceCompany")
                    ?.also { ensureQuery().insuranceCompany = it.jsonPrimitive.content }
                json.jsonObject.get("area")?.also { ensureQuery().area = it.jsonPrimitive.content }
                json.jsonObject.get("intent")?.also { intent = Intent.fromString(it.jsonPrimitive.content) }
                this
            }
        }

        private fun ensureQuery() = (this.query ?: Query().also { this.query = it })

        fun queryResult(data: Data): QueryResult? {
            return this.query?.let { data.query(it) }
        }


        override fun byInput(understanding: Understanding): Pair<State, Request> {
            return when (understanding) {
                is Understanding.Json -> {
                    val result = this.dumpBy(understanding.jsonElement)
                    result.fold(
                        onSuccess = {
                            it to Request.Result
                        },
                        onFailure = {
                            Pair(this@Chatting, Request.Failed(it.message ?: ""))
                        }
                    )
                }

                is Understanding.NothingFind -> {
                    this to Request.QueryRequest(listOf())
                }

                is Understanding.Failed -> {
                    Pair(Start, Request.Failed(understanding.message ?: ""))
                }
            }
        }

    }

}


@Component
class Session(@Autowired val data: Data) {
    var state: State = State.Start


    fun input(understanding: Understanding): Request {
        val o = this.state.byInput(understanding)
        this.state = o.first
        return o.second
    }

    fun restart() {
        this.state = State.Start
    }

    fun result(): QueryResult? {
        return when (this.state) {
            is State.Chatting -> (this.state as State.Chatting).queryResult(data)
            else -> error("not support state")
        }
    }
}

class Intent(val value: String) {
    companion object {
        fun fromString(string: String): Intent {
            return Intent(string)
        }
    }
}
