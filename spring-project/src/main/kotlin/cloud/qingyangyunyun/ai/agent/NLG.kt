package cloud.qingyangyunyun.ai.agent

fun toOutput(reply: Reply): Output {
    return when (reply) {
        is Reply.IntentRequest -> intentRequest().plain()

        is Reply.SlotRequest -> slotsRequest(reply.names).plain()

        is Reply.Fallback -> fallback().plain()

        is Reply.Ending -> bye().plain()

        is Reply.Help -> help().plain()

        is Reply.Greeting -> greeting().plain()

        is Reply.General -> reply.text.plain()

        is Reply.Result -> reply.data.toOutput()

        is Reply.Composite -> reply.replies.map { toOutput(it) }.reduce { acc, output -> acc + output }

        else -> fallback().plain()
    }
}