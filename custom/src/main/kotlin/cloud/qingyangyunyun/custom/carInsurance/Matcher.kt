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

//通配词
val alwaysKeywords = listOf("不限", "不限制", "无要求", "无限制")
// 同义词
val synonyms = listOf(setOf("除成都外的四川地区", "成都以外","地市区","省内"))

//TODO 概括词
//比如网约车，包括燃油网约和新能源网约车

//TODO 词语们可能需要分字段？

fun synonym(word: String): Set<String> {
    return synonyms.find {
        it.contains(word)
    }?.minus(word) ?: emptySet()
}

class ContentMatcher(val str: String, val index: Int) : Matcher {
    override fun match(line: List<String>): Boolean {
        return line[index].trim().let {
            it == (str) || it in synonym(str) || it in alwaysKeywords || (it.contains(str) && it.contains("或"))
        }
    }
}

fun Query.matcher(): Matcher {
    val matchers = mutableListOf<Matcher>()
    if (carModel != null) {
        matchers.add(ContentMatcher(carModel!!, 0))
    }
    if (area != null) {
        matchers.add(ContentMatcher(area!!, 1))
    }
    if (insuranceCompany != null) {
        matchers.add(ContentMatcher(insuranceCompany!!, 2))
    }

    if (carOwner != null) {
        matchers.add(ContentMatcher(carOwner!!, 3))
    }
    return if (matchers.isEmpty()) {
        tureMatcher()
    } else {
        AndMatcher(matchers)
    }
}

fun Query2.matcher(): Matcher {
    val matchers = mutableListOf<Matcher>()
    if (insuranceCompany != null) {
        matchers.add(ContentMatcher(insuranceCompany!!, 0))
    }
    return if (matchers.isEmpty()) {
        tureMatcher()
    } else {
        AndMatcher(matchers)
    }
}