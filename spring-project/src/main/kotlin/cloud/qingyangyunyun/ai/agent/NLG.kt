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

        is Reply.Result -> reply.data.toString().plain() //TODO

        else -> fallback().plain()
    }
}