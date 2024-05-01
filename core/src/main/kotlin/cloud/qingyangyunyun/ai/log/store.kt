package cloud.qingyangyunyun.ai.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nietongxue.common.base.LogLevel
import xyz.nietongxue.common.log.Log

@Component
class LogStore(
    @Autowired
    var listener: LogSender
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(LogStore::class.java)
    }

    private val logs = mutableListOf<Log<String>>()
    fun add(log: String, level: LogLevel = LogLevel.INFO) {
        Log(log, level).also {
            logs.add(it)
            listener.sendLog(it)
            when (level) {
                LogLevel.ERROR -> logger.error(log)
                LogLevel.WARN -> logger.warn(log)
                else -> logger.info(log)
            }
        }
    }

    fun getLogs(): List<Log<String>> {
        return logs
    }

    fun clear() {
        logs.clear()
    }
}