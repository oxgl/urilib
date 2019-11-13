package com.oxyggen.net

import com.oxyggen.io.Path

// Common URL
open class CommonURL(uriString: String, context: ContextURI? = null) : URL(uriString, context) {

    private val pattern = "^((?<scheme>[^:/?#]+):)?(//(?<authority>((?<userinfo>[^/?#]*)@)?(?<host>[^/?#:]*)(:(?<port>[^/?#]*))?))?(?<path>[^?#]*)(\\?(?<query>[^#]*))?(#(?<fragment>.*))?".toRegex()

    val userinfo: String
    val host: String
    val uriPort: Int
    val port: Int get() = if (uriPort > 0) uriPort else getDefaultPort()
    val path: Path
    val query: String
    val fragment: String

    /**
     * The default port for current scheme (http = 80, https = 443,...)
     * @return the default port
     */
    protected open fun getDefaultPort() = -1

    init {

        val match = pattern.matchEntire(uriString)

        if (match?.groups?.get("authority")?.value.isNullOrBlank()) {
            if (context is CommonURL) {
                userinfo = context.userinfo
                host = context.host
                uriPort = context.uriPort
            } else {
                throw Exception("Can't handle relative uri $uriString without context!")
            }
        } else {
            userinfo = match?.groups?.get("userinfo")?.value ?: ""
            host = match?.groups?.get("host")?.value ?: ""
            val strPort = match?.groups?.get("port")?.value?.trim() ?: ""
            val foundPort = if (strPort.isBlank()) {
                0
            } else {
                strPort.toInt()
            }
            uriPort = if (foundPort > 0) {
                foundPort
            } else {
                -1
            }
        }

        path = Path.parse(percentDecode(match?.groups?.get("path")?.value ?: ""))
        query = percentDecode(match?.groups?.get("query")?.value ?: "")
        fragment = percentDecode(match?.groups?.get("fragment")?.value ?: "")
    }

    /**
     * The normalized Uri string
     * @return the normalized Uri string
     */
    open fun toNormalizedUriString(): String {
        // Scheme
        var result = "$scheme://"

        // User info (if exists)
        if (userinfo.isNotEmpty()) result += "$userinfo@"

        // Host (always)
        result += host

        // Port only when it's not the defualt port
        if (getDefaultPort() != port)
            result += ":$port"

        // Normalized path
        result += path.normalized.complete

        // Query
        if (query.isNotEmpty())
            result += "?$query"

        // Fragment
        if (fragment.isNotEmpty())
            result += "#$fragment"

        return result
    }

    /**
     * The CommonURL object containing normalized path
     * @return the CommonURL object containing normalized path
     */
    open fun toNormalizedURL() = parse(toNormalizedUriString()) as CommonURL

}