package com.oxyggen.matcher

open class RegexMatcher(regexPattern: String) : Matcher() {

    private val regex: Regex = Regex(regexPattern)

    open fun toRegex() = regex

    override fun matches(str: CharSequence): Boolean = regex.matches(str)

}