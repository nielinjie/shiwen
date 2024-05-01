package cloud.qingyangyunyun.custom.carInsurance

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cloud.qingyangyunyun.ai.agent.*

const val TERM_RESET = "重置"
const val TERM_QUERY = "查询"




val carDefine = object : IntentsDefine {
    val data = Data()
    override val intentDefs = defs
    override fun validateIntent(holding: IntentHolding): Either<String, IntentHolding> {
        return super.validateIntent(holding) // .flatMap() //checking slot.
    }


    fun IntentHolding.query(): Either<String, Either<Query, Query2>> {
        return try {
            if (this.slotValue("dataset") == null)
                Query().also {
                    it.carModel = slotValue("carModel")
                    it.area = slotValue("area")
                    it.insuranceCompany = slotValue("insuranceCompany")
                    it.carOwner = slotValue("carOwner")
                    it.target = slotValue("target")
                }.left().right()
            else
                Query2().also {
                    it.insuranceCompany = slotValue("insuranceCompany")
                }.right().right()

        } catch (e: Exception) {
            return (e.message ?: e.toString()).left()
        }
    }

    fun QueryResult.toRunnerResult(fieldName: String?): RunnerResult {
        return object : RunnerResult {
            override fun toOutput(): Output {
                return displayResult(this@toRunnerResult, fieldName)
            }
        }
    }

    fun QueryResult2.toRunnerResult(fieldName: String?): RunnerResult {
        return object : RunnerResult {
            override fun toOutput(): Output {
                return displayResult2(this@toRunnerResult, fieldName)
            }
        }
    }


    override fun run(holding: IntentHolding, currentState: State): State {
        require(holding.intent.isSome())
        return when (holding.intent.getOrNull()?.name) {
            TERM_RESET -> {
                State.Start
            }

            TERM_QUERY -> {
                val query = holding.query()
                val queryResult: Either<String, Either<QueryResult, QueryResult2>> = query.map {
                    when (it) {
                        is Either.Left -> data.query(it.value).left()
                        is Either.Right -> data.query2(it.value).right()
                    }
                }
                queryResult.fold(
                    { State.Failed(it, holding) },
                    {
                        State.Result(
                            when (it) {
                                is Either.Left -> it.value.toRunnerResult(
                                    holding.slotValue("target")
                                )

                                is Either.Right -> it.value.toRunnerResult(
                                    holding.slotValue("target")
                                )
                            },
                            holding
                        )
                    }
                )
            }

            else -> currentState // do nothing if not specified
        }
    }
}