package cloud.qingyangyunyun.custom.carInsurance

interface Matcher {
    fun match(line: List<String>): Boolean
}

class AndMatcher(val matchers: List<Matcher>) : Matcher {
    override fun match(line: List<String>): Boolean {
        return matchers.all { it.match(line) }
    }
}

fun Matcher.and(matcher: Matcher) = AndMatcher(listOf(this, matcher))

class OrMatcher(val matchers: List<Matcher>) : Matcher {
    override fun match(line: List<String>): Boolean {
        return matchers.any { it.match(line) }
    }
}

fun Matcher.or(matcher: Matcher) = OrMatcher(listOf(this, matcher))
fun tureMatcher() = object : Matcher {
    override fun match(line: List<String>): Boolean {
        return true
    }
}

class NotMatcher(val matcher: Matcher) : Matcher {
    override fun match(line: List<String>): Boolean {
        return !matcher.match(line)
    }
}

val noConditionKey = listOf("不限", "不限制", "无要求", "无限制")

class StringMather(val str: String, val index: Int) : Matcher {
    override fun match(line: List<String>): Boolean {
        return line[index].trim().let {
            it == (str) || it in noConditionKey || (it.contains(str) && it.contains("或"))
        }
    }
}

fun Query.matcher(): Matcher {
    val matchers = mutableListOf<Matcher>()
    if (carModel != null) {
        matchers.add(StringMather(carModel!!, 0))
    }
    if (area != null) {
        matchers.add(StringMather(area!!, 1))
    }
    if (insuranceCompany != null) {
        matchers.add(StringMather(insuranceCompany!!, 2))
    }

    if (carOwner != null) {
        matchers.add(StringMather(carOwner!!, 3))
    }
    return if (matchers.isEmpty()) {
        tureMatcher()
    } else {
        AndMatcher(matchers)
    }
}