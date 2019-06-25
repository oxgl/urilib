package com.oxyggen.io

import com.oxyggen.io.Path
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PathTest {
    @Test
    fun `Path normaization test`() {
        Assertions.assertEquals("/first/second/fourth", Path("/first/second/third/../fourth").normalized)
        Assertions.assertEquals("/first/second/fourth/", Path("/first/second/third/../fourth/").normalized)
        Assertions.assertEquals("./first/second/fourth/", Path("./first/second/third/../fourth/").normalized)
        Assertions.assertEquals("./first/second/fourth/", Path("./first/second/third/../fourth/.").normalized)
        Assertions.assertEquals("/a/c/d.html", Path("/../a/b/../c/./d.html").normalized)
    }

    @Test
    fun `Path filename test`() {
        Assertions.assertEquals("fourth", Path("/first/second/third/../fourth").file)
        Assertions.assertEquals("", Path("/first/second/third/../fourth/").file)
        Assertions.assertEquals("", Path("./first/second/third/../fourth/").file)
        Assertions.assertEquals("", Path("./first/second/third/../fourth/.").file)
        Assertions.assertEquals("d.html", Path("/../a/b/../c/./d.html").file)
    }

    @Test
    fun `Path directory test`() {
        Assertions.assertEquals("/first/second/third/../", Path("/first/second/third/../fourth").directory)
        Assertions.assertEquals("/first/second/third/../fourth/", Path("/first/second/third/../fourth/").directory)
        Assertions.assertEquals("./first/second/third/../fourth/", Path("./first/second/third/../fourth/").directory)
        Assertions.assertEquals("./first/second/third/../fourth/", Path("./first/second/third/../fourth/.").directory)
        Assertions.assertEquals("/../a/b/../c/./", Path("/../a/b/../c/./d.html").directory)
    }

    @Test
    fun `Path resolving test`() {
        val basePath = Path("/../a/b/../c/./abc.htm")

        Assertions.assertEquals("/a/c/abc.htm", basePath.normalized)
        Assertions.assertEquals("/a/c/def.htm", basePath.resolve("./def.htm").normalized)
        Assertions.assertEquals("/a/x1.htm", basePath.resolve("../x1.htm").normalized)
        Assertions.assertEquals("/x2.htm", basePath.resolve("/x2.htm").normalized)

    }

}
