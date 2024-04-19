package cloud.qingyangyunyun.ai.agent.car

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cloud.qingyangyunyun.ai.agent.*
import cloud.qingyangyunyun.custom.carInsurance.Data
import cloud.qingyangyunyun.custom.carInsurance.Query

val carDefine = object : Define {
    val data = Data()
    override val intentDefs = defs
    override fun validateIntent(holding: IntentHolding): Either<String, IntentHolding> {
        require(holding.intent.isSome())
        return when (holding.intent.getOrNull()?.name) {
            "重置" -> {
                (holding).right()
            }

            "查询" -> {
                (holding).right()
            }

            null -> "no intent find".left()

            else -> ("not support intent").left()
        }
    }


    fun IntentHolding.query(): Either<String, Query> {
        return try {
            Query().also {
                it.carModel = slotValue("carModel")
                it.area = slotValue("area")
                it.insuranceCompany = slotValue("insuranceCompany")
                it.carOwner = slotValue("carOwner")
                it.target = slotValue("target")

            }.right()
        } catch (e: Exception) {
            return (e.message ?: e.toString()).left()
        }
    }

    fun cloud.qingyangyunyun.custom.carInsurance.QueryResult.toRunnerResult(fieldName: String?): RunnerResult {
        return object : RunnerResult {
            override fun toOutput(): Output {
                return displayResult(this@toRunnerResult, fieldName)
            }
        }
    }

    override fun run(holding: IntentHolding,currentState: State): State {
        require(holding.intent.isSome())
        return when (holding.intent.getOrNull()?.name) {
            "重置" -> {
                State.Start
            }

            "查询" -> {
                val query = holding.query()
                query.map {
                    data.query(it)
                }.fold(
                    { State.Failed(it) },
                    {
                        State.Result(
                            it.toRunnerResult(
                                holding.slotValue("target")
                            )
                        )
                    }
                )
            }

            else -> error("not support")
        }
    }
}