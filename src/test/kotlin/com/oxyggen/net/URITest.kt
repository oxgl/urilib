package com.oxyggen.net

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class URITest {
    @Test
    fun `Absolute URL parsing`() {

        val u1 = URI.parse("https://test.user@subdomain.domain.com:8080/my/path/../to/file.htm?name=u&other=z#hash_value")

        Assertions.assertTrue(u1 is HttpURL)

        if (u1 is CommonURL) {
            Assertions.assertEquals("https", u1.scheme, "scheme")
            Assertions.assertEquals("test.user", u1.userinfo, "userinfo")
            Assertions.assertEquals("subdomain.domain.com", u1.host, "host")
            Assertions.assertEquals(8080, u1.port, "port")
            Assertions.assertEquals("/my/path/../to/file.htm", u1.path.complete, "path")
            Assertions.assertEquals("name=u&other=z", u1.query, "query")
            Assertions.assertEquals("hash_value", u1.fragment, "fragment")
        }


        val u2 = URI.parse("https://test.user@subdomain.domain.com#hash_value")

        Assertions.assertTrue(u2 is HttpURL)
        if (u2 is CommonURL) {
            Assertions.assertEquals("https", u2.scheme, "scheme")
            Assertions.assertEquals("test.user", u2.userinfo, "userinfo")
            Assertions.assertEquals("subdomain.domain.com", u2.host, "host")
            Assertions.assertEquals(443, u2.port, "port")
            Assertions.assertEquals("/", u2.path.complete, "path")
            Assertions.assertEquals("", u2.query, "query")
            Assertions.assertEquals("hash_value", u2.fragment, "fragment")
        }

        val u3 = URI.parse("http://subdomain.domain.com:81?test=a%20b")

        Assertions.assertTrue(u3 is HttpURL)
        if (u3 is CommonURL) {
            Assertions.assertEquals("http", u3.scheme, "scheme")
            Assertions.assertEquals("", u3.userinfo, "userinfo")
            Assertions.assertEquals("subdomain.domain.com", u3.host, "host")
            Assertions.assertEquals(81, u3.port, "port")
            Assertions.assertEquals("/", u3.path.complete, "path")
            Assertions.assertEquals("test=a%20b", u3.query, "query")
            Assertions.assertEquals("", u3.fragment, "fragment")
        }

        val u4 = URI.parse("http://subdomain.domain.com/?test=c+d")

        Assertions.assertTrue(u4 is HttpURL)
        if (u4 is CommonURL) {
            Assertions.assertEquals("http", u4.scheme, "scheme")
            Assertions.assertEquals("", u4.userinfo, "userinfo")
            Assertions.assertEquals("subdomain.domain.com", u4.host, "host")
            Assertions.assertEquals(80, u4.port, "port")
            Assertions.assertEquals("/", u4.path.complete, "path")
            Assertions.assertEquals("test=c+d", u4.query, "query")
            Assertions.assertEquals("", u4.fragment, "fragment")
        }


    }

    @Test
    fun `Relative URL parsing`() {

        val u = URI.parse("https://test.user@subdomain.domain.com:8080/my/path/to/file.htm?name=u&other=z#hash_value")

        Assertions.assertTrue(u is ContextURI, "Should be context URI!")

        if (u is ContextURI) {
            val r = URI.parse("/different/path/index.htm?name=r#new_hash", u)

            Assertions.assertTrue(u is HttpURL)
            if (r is CommonURL) {
                Assertions.assertEquals("https", r.scheme, "scheme")
                Assertions.assertEquals("test.user", r.userinfo, "userinfo")
                Assertions.assertEquals("subdomain.domain.com", r.host, "host")
                Assertions.assertEquals(8080, r.port, "port")
                Assertions.assertEquals("/different/path/index.htm", r.path.complete, "path")
                Assertions.assertEquals("name=r", r.query, "query")
                Assertions.assertEquals("new_hash", r.fragment, "fragment")
            }
        }

        val ur = URI.parse("/different/path.htm?param=x1")

        Assertions.assertTrue(ur is UnresolvedURI)

        if (u is ContextURI) {
            val u2 = u.resolve(ur)

            Assertions.assertTrue(u2 is CommonURL)

            if (u2 is CommonURL) {
                Assertions.assertEquals("https", u2.scheme, "scheme")
                Assertions.assertEquals("test.user", u2.userinfo, "userinfo")
                Assertions.assertEquals("subdomain.domain.com", u2.host, "host")
                Assertions.assertEquals(8080, u2.port, "port")
                Assertions.assertEquals("/different/path.htm", u2.path.complete, "path")
                Assertions.assertEquals("param=x1", u2.query, "query")
            }
        }
    }

    @Test
    fun `Relative URL path parsing`() {
        val u1 = URI.parse("http://subdomain.domain.com/abc/?test=c+d")

        val u2 = URI.parse("test.html", u1 as ContextURI)

        Assertions.assertTrue(u2 is HttpURL)
        if (u2 is CommonURL) {
            println(u2.toResolvedUriString())
            Assertions.assertEquals("http", u2.scheme, "scheme")
            Assertions.assertEquals("", u2.userinfo, "userinfo")
            Assertions.assertEquals("subdomain.domain.com", u2.host, "host")
            Assertions.assertEquals(80, u2.port, "port")
            Assertions.assertEquals("/abc/test.html", u2.path.complete, "path")
            Assertions.assertEquals("", u2.query, "query")
            Assertions.assertEquals("", u2.fragment, "fragment")
        }
    }

    @Test
    fun `URL encoding test`() {
        val original = "hd_ÁÖüÜűé_€_ह_¢_!%abc!a"
        println(original)
        val encoded = URI.percentEncode(original)
        println(encoded)
        val decoded = URI.percentDecode(encoded)
        Assertions.assertEquals(original, decoded)
    }

    @Test
    fun `Mailto parsing`() {
        val u = URI.parse("https://test.user@subdomain.domain.com:8080/my/path/to/file.htm?name=u&other=z#hash_value")

        println(u)

        val m = URI.parse("mailto:test@abc.com")
        Assertions.assertTrue(m is MailtoURI)

        if (u is ContextURI) {
            val m2 = u.resolve(m)

            Assertions.assertEquals(m, m2)

            println(m2.toString())
        }

    }

    @Test
    fun `HTTP URL parameters test`() {
        val u = URI.parse("https://test.user@subdomain.domain.com:8080/my/path/to/file.htm?name=first&name=last&other=z#hash_value")

        if (u is HttpURL) {
            Assertions.assertEquals(u.queryParam["name"]?.first(), "first")
            Assertions.assertEquals(u.queryParam["name"]?.get(1), "last")
        }

    }

}