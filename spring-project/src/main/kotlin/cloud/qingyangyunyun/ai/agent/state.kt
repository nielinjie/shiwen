package cloud.qingyangyunyun.ai.agent


class StateMachine(val define: Define) {

    var currentState: State = State.Start
    fun onEvent(event: Event) {
        val newState = currentState.next(event, this)
        if (newState != currentState) {
            currentState = newState
            this.onEvent(StateChanged(newState))
        }
    }
}

interface Event
interface Input : Event {
    data class UserInput(val underStood: UnderStood) : Input
}

data class StateChanged(val newState: State) : Event

interface State {
    fun next(event: Event, machine: StateMachine): State


    fun Event.useIntents(f: (UnderStood.Intents) -> State): State {
        return when (this@useIntents) {
            is Input.UserInput -> when (this.underStood) {
                is UnderStood.Intents -> f(this.underStood)
                else -> this@State
            }

            else -> this@State
        }
    }

    data object Start : State {
        override fun next(event: Event, machine: StateMachine): State {
            return event.useIntents {
                if (it.holding.hasIntent()) {
                    val intentHolding = it.holding.copy()
                    if (intentHolding.satisfied(machine.define)) {
                        Satisfied(intentHolding)
                    } else {
                        WithIntent(intentHolding)
                    }
                } else {
                    WithSlot(it.holding.copy())
                }
            }
        }
    }

    data class WithSlot(val holding: IntentHolding) : State {
        override fun next(event: Event, machine: StateMachine): State {
            return event.useIntents {
                if (it.holding.hasIntent()) {
                    val intentHolding = this@WithSlot.holding.merge(it.holding, machine.define)
                    if (intentHolding.satisfied(machine.define)) {
                        Satisfied(intentHolding)
                    } else {
                        WithIntent(intentHolding)
                    }
                } else {
                    this
                }
            }
        }
    }

    data class WithIntent(val holding: IntentHolding) : State {
        fun getMissSlots(): List<SlotDef> {
            TODO()
        }

        override fun next(event: Event, machine: StateMachine): State {
            return event.useIntents {
                val intentHolding = this@WithIntent.holding.merge(it.holding, machine.define)
                if (intentHolding.satisfied(machine.define)) {
                    Satisfied(intentHolding)
                } else {
                    WithIntent(intentHolding)
                }
            }
        }
    }

    data class Satisfied(val holding: IntentHolding) : State {
        override fun next(event: Event, machine: StateMachine): State {
            return when (event) {
                is StateChanged -> {
                    if (event.newState == this) {
                        try {
                            machine.define.run(holding, this)
                        } catch (e: Exception) {
                            State.Failed(e.message ?: e.toString(), holding)
                        }
                    } else {
                        this
                    }
                }

                else -> event.useIntents {
                    val intentHolding = this@Satisfied.holding.merge(it.holding, machine.define)
                    if (intentHolding.satisfied(machine.define)) {
                        Satisfied(intentHolding)
                    } else {
                        WithIntent(intentHolding)
                    }
                }
            }
        }
    }

    data class Failed(val message: String, val holding: IntentHolding) : State {
        override fun next(event: Event, machine: StateMachine): State {
            return event.useIntents {
                if (it.holding.hasIntent()) {
                    val intentHolding = this@Failed.holding.merge(it.holding, machine.define)
                    if (intentHolding.satisfied(machine.define)) Satisfied(intentHolding) else WithIntent(intentHolding)
                } else WithSlot(it.holding.copy())
            }
        }
    }

    data class Result(val result: RunnerResult, val holding: IntentHolding) : State {
        override fun next(event: Event, machine: StateMachine): State {
            return event.useIntents {
                if (it.holding.hasIntent()) {
                    val intentHolding = this@Result.holding.merge(it.holding, machine.define)
                    if (intentHolding.satisfied(machine.define)) {
                        Satisfied(intentHolding)
                    } else {
                        WithIntent(intentHolding)
                    }
                } else {
                    WithSlot(it.holding.copy())
                }
            }
        }
    }
}