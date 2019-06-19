package com.oxyggen.net

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class URITest {
    @Test
    fun `test parser`() {
        val u = URI.parse("http://abc.com")

        println(u.scheme)
        println(u.identifier)
        println(u::class)

        Assertions.assertTrue(u is URL)
        Assertions.assertTrue(u is HttpURL)
    }
}