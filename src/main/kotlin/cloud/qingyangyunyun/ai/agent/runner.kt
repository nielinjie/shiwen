package cloud.qingyangyunyun.ai.agent

interface RunnerResult{
    fun toOutput(): Output
}

interface Runner {
    fun run(holding: IntentHolding): State
}

interface Data {
    fun query(intent: GotIntent, slots: List<GotSlot>): QueryResult
}

interface QueryResult {
    val items: List<QueryResultItem>
}

interface QueryResultItem

