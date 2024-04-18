package cloud.qingyangyunyun.ai.log

import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.LogLevel
import xyz.nietongxue.common.log.Log

@Component
class LogStore(
    var listener: ((Log<String>) -> Unit)? = null
) {
    private val logs = mutableListOf<Log<String>>()
    fun add(log: String, level: LogLevel = LogLevel.INFO) {
        Log(log, level).also {
            logs.add(it)
            listener?.invoke(it)
        }
    }

    fun getLogs(): List<Log<String>> {
        return logs
    }

    fun clear() {
        logs.clear()
    }
}