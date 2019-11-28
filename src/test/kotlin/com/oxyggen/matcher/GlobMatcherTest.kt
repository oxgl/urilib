package com.oxyggen.matcher

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GlobMatcherTest {
    @Test
    fun `glob to regexp tester`() {
        val gm = GlobMatcher("*/*.txt")

        println(gm.toRegex().pattern)

        Assertions.assertTrue(gm.matches("hello.txt"))
        Assertions.assertFalse(gm.matches("hello.txt.exe"))

        Assertions.assertTrue(gm.matches("abc/hello.txt"))
    }

}