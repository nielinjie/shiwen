package cloud.qingyangyunyun.ai.chat

import cloud.qingyangyunyun.custom.carInsurance.Data
import cloud.qingyangyunyun.custom.carInsurance.Query
import cloud.qingyangyunyun.custom.carInsurance.QueryResult
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed interface State {
    fun byInput(understanding: Understanding): Pair<State, Replay>

    data object Start : State {
        override fun byInput(understanding: Understanding): Pair<State, Replay> {
            return when (understanding) {
                is Understanding.Querying -> {
                    val result = Chatting.fromJson(understanding)
                    result.fold(
                        onSuccess = {
                            it to when (it.intent) {
                                null -> (Replay.IntentRequest).and(Replay.ShortedResult)
                                else -> Replay.Result
                            }
                        },
                        onFailure = {
                            Pair(Start, Replay.Failed(it.message ?: ""))
                        }
                    )
                }
                // 清空、招呼、告别、帮助
                is Understanding.Command -> {
                    when (understanding.command) {
                        "清空" -> Pair(Start, Replay.Blah("查询已重置, 可以重新开始。").and(Replay.Helping))
                        "招呼" -> Pair(this, Replay.Blah("你好，欢迎。").and(Replay.Helping))
                        "告别" -> Pair(Start, Replay.Blah("再见，欢迎再次使用。"))
                        "帮助" -> Pair(this, Replay.Helping)
                        else -> Pair(this, Replay.Failed("不支持的命令"))
                    }
                }

                is Understanding.NothingFind -> {
                    Start to Replay.QueryRequest(listOf())
                }

                is Understanding.Failed -> {
                    Pair(Start, Replay.Failed(understanding.message ?: ""))
                }
            }
        }
    }

    data class Chatting(
        var query: Query? = null,
        var intent: Intent? = null
    ) : State {
        companion object {
            fun fromJson(ujson: Understanding.Querying): Result<Chatting> {
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

        fun queryResult(data: Data): QueryResult {
            return this.query.let { data.query(it) }
        }


        override fun byInput(understanding: Understanding): Pair<State, Replay> {
            return when (understanding) {
                is Understanding.Querying -> {
                    val result = this.dumpBy(understanding.jsonElement)
                    result.fold(
                        onSuccess = {
                            val nextState =
                                if (understanding.goon()) this else Chatting().also { it.dumpBy(understanding.jsonElement) }
                            val replay =
//                                if ((nextState as? Chatting)?.query == null)
//                                    Replay.QueryRequest(listOf()).and(Replay.ShortedResult)
//                                else
                                Replay.Result
                            Pair(nextState, replay)
                        },
                        onFailure = {
                            Pair(this@Chatting, Replay.Failed(it.message ?: ""))
                        }
                    )
                }

                is Understanding.Command -> {
                    when (understanding.command) {
                        "清空" -> Pair(Start, Replay.Blah("查询已重置, 可以重新开始。").and(Replay.Helping))
                        "继续" -> Pair(this, Replay.Result)
                        "招呼" -> Pair(this, Replay.Blah("你好，欢迎。").and(Replay.Helping))
                        "告别" -> Pair(Start, Replay.Blah("再见，欢迎再次使用。"))
                        "帮助" -> Pair(this, Replay.Helping)
                        else -> Pair(this, Replay.Failed("不支持的命令"))
                    }
                }

                is Understanding.NothingFind -> {
                    this to Replay.QueryRequest(listOf())
                }

                is Understanding.Failed -> {
                    Pair(Start, Replay.Failed(understanding.message ?: ""))
                }
            }
        }

    }

}