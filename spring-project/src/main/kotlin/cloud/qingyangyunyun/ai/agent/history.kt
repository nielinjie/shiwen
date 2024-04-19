package cloud.qingyangyunyun.ai.agent

data class Role(val name: String)
class History {
    private var history = mutableListOf<Pair<Role, String>>()
    fun appendUser(string: String) {
        history += Pair(Role("user"), string)
    }

    fun appendAgent(string: String) {
        history += Pair(Role("agent"), string)
    }
    fun toList(): List<Pair<Role, String>> {
        return history
    }
}