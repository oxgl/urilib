package com.oxyggen.io

import com.oxyggen.io.Path
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PathTest {
    @Test
    fun `Path normaization test`() {
        Assertions.assertEquals("/first/second/", Path.parse("/first/second/third/../fourth").normalizedPath.directory)
        Assertions.assertEquals("/first/second/fourth/", Path.parse("/first/second/third/../fourth/").normalizedPath.directory)
        Assertions.assertEquals("./first/second/fourth/", Path.parse("./first/second/third/../fourth/").normalizedPath.directory)
        Assertions.assertEquals("./first/second/fourth/", Path.parse("./first/second/third/../fourth/.").normalizedPath.directory)
        Assertions.assertEquals("./a/c/", Path.parse("./../a/b/../c/./d.html").normalizedPath.directory)
        Assertions.assertEquals("/", Path.parse("/").normalizedPath.directory)
        Assertions.assertEquals("./dev/", Path.parse("./dev/").normalizedPath.directory)
    }

    @Test
    fun `Path filename test`() {
        Assertions.assertEquals("fourth", Path.parse("/first/second/third/../fourth").file)
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
        Assertions.assertEquals("./first/second/third/../fourth/", Path.parse("./first/second/third/../fourth/.").directory)
        Assertions.assertEquals("/../a/b/../c/./", Path.parse("/../a/b/../c/./d.html").directory)
    }

    @Test
    fun `Path resolving test`() {
        val basePath = Path.parse("/../a/b/../c/./abc.htm")

        Assertions.assertEquals("/a/c/abc.htm", basePath.normalizedPath.full)
        Assertions.assertEquals("/a/c/def.htm", basePath.resolve("./def.htm").normalizedPath.full)
        Assertions.assertEquals("/a/x1.htm", basePath.resolve("../x1.htm").normalizedPath.full)
        Assertions.assertEquals("/x2.htm", basePath.resolve("/x2.htm").normalizedPath.full)

    }

}
