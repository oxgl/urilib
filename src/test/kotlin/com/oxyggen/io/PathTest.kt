package com.oxyggen.io

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PathTest {
    @Test
    fun `Path normaization test`() {
        Assertions.assertEquals("/first/second/", Path.parse("/first/second/third/../fourth").normalized.directory)
        Assertions.assertEquals("/first/second/fourth/", Path.parse("/first/second/third/../fourth/").normalized.directory)
        Assertions.assertEquals("./first/second/fourth/", Path.parse("./first/second/third/../fourth/").normalized.directory)
        Assertions.assertEquals("./first/second/fourth/", Path.parse("./first/second/third/../fourth/.").normalized.directory)
        Assertions.assertEquals("./a/c/", Path.parse("./../a/b/../c/./d.html").normalized.directory)
        Assertions.assertEquals("/", Path.parse("/").normalized.directory)
        Assertions.assertEquals("./dev/", Path.parse("./dev/").normalized.directory)
    }

    @Test
    fun `Path filename test`() {
        Assertions.assertEquals("fourth", Path.parse("/first/second/third/../fourth").file)
        Assertions.assertEquals("", Path.parse("/first/second/third/../fourth/").file)
        Assertions.assertEquals("", Path.parse("/first/second/third/../fourth/").file)
        Assertions.assertEquals("", Path.parse("./first/second/third/../fourth/").file)
        Assertions.assertEquals("", Path.parse("./first/second/third/../fourth/.").file)
        Assertions.assertEquals("d.html", Path.parse("/../a/b/../c/./d.html").file)
    }

    @Test
    fun `Path directory test`() {
        Assertions.assertEquals("/first/second/third/../", Path.parse("/first/second/third/../fourth").directory)
        Assertions.assertEquals("/first/second/third/../fourth/", Path.parse("/first/second/third/../fourth/").directory)
        Assertions.assertEquals("./first/second/third/../fourth/", Path.parse("./first/second/third/../fourth/").directory)
        Assertions.assertEquals("./first/second/third/../fourth/./", Path.parse("./first/second/third/../fourth/.").directory)
        Assertions.assertEquals("/../a/b/../c/./", Path.parse("/../a/b/../c/./d.html").directory)
    }

    @Test
    fun `Path resolving test`() {
        val basePath = Path.parse("/../a/b/../c/./abc.htm")

        Assertions.assertEquals("/a/c/abc.htm", basePath.normalized.complete)
        Assertions.assertEquals("/a/c/def.htm", basePath.resolve("./def.htm").normalized.complete)
        Assertions.assertEquals("/a/x1.htm", basePath.resolve("../x1.htm").normalized.complete)
        Assertions.assertEquals("/x2.htm", basePath.resolve("/x2.htm").normalized.complete)
    }

    @Test
    fun `Hash code test`() {
        Assertions.assertTrue(Path.parse("/test/abc/").equals(Path.parse("/test/abc/")))
        Assertions.assertTrue(Path.parse("/test/abc/.").normalized.equals(Path.parse("/test/abc/").normalized))
    }

    fun `Path attributes`() {
        val p1 = Path.parse("/")
        Assertions.assertTrue(p1.isAbsolute)
        Assertions.assertEquals("/", p1.complete)
        Assertions.assertEquals("/", p1.directory)
    }

}
