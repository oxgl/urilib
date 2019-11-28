package com.oxyggen.matcher

abstract class Matcher() {
    abstract fun matches(str: CharSequence): Boolean
}