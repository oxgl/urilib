package com.oxyggen.matcher

open class RegexMatcher(regexPattern: String, options: Set<RegexOption>) : Matcher() {

    private val regex: Regex = Regex(regexPattern, options)

    open fun toRegex() = regex

    override fun matches(str: CharSequence): Boolean = regex.matches(str)

}