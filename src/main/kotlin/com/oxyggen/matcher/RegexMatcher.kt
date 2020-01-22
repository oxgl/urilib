package com.oxyggen.matcher

open class RegexMatcher(regexPattern: String, ignoreCase: Boolean = true) : Matcher() {

    private val regex: Regex = Regex(regexPattern, if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else setOf())

    open fun toRegex() = regex

    override fun matches(str: CharSequence): Boolean = regex.matches(str)

}