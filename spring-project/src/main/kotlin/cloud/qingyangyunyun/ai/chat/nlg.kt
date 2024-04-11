package cloud.qingyangyunyun.ai.chat

import org.springframework.stereotype.Component

sealed interface Request {
    object IntentRequest : Request
    object None : Request
    class Failed(val reason: String) : Request
    class QueryRequest(val propertyName: List<String>) : Request
    object Result : Request
}
@Component
class NLG() {
    fun output(request: Request? = Request.None, input: String, session: Session): String {
        return when (request) {

            is Request.IntentRequest -> {
                "指定查询意图"
            }

            is Request.None -> {
                "例子。。。"
            }

            is Request.Failed -> {
                request.reason + "\n失败，请稍后尝试。"
            }

            is Request.QueryRequest -> {
                "请告诉我" + request.propertyName.ifEmpty { listOf("车型") }
                    .joinToString("、")
            }

            is Request.Result -> {
                session.result().toString()
            }

            null -> error("not support code")
        }
    }
}