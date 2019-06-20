package com.oxyggen.net

import java.lang.Exception
import java.net.URLDecoder

// Common URL
open class CommonURL(uriString: String, context: URI? = null) : URL(uriString, context) {

    //original: "^((?<scheme>[^:\\/?#]+):)?(\\/\\/(?<authority>((?<userinfo>[^\\/?#]*)@)?(?<host>[^\\/?#:]*)(:(?<port>[^\\/?#]*))?))?(?<path>[^?#]*)(\\?(?<query>[^#]*))?(#(?<fragment>.*))?".toRegex()

    private val pattern = "^((?<scheme>[^:/?#]+):)?(//(?<authority>((?<userinfo>[^/?#]*)@)?(?<host>[^/?#:]*)(:(?<port>[^/?#]*))?))?(?<path>[^?#]*)(\\?(?<query>[^#]*))?(#(?<fragment>.*))?".toRegex()

    val userinfo: String
    val host: String
    val port: Int get() = if (field > 0) field else getDefaultPort()
    val path: String
    val query: String
    val fragment: String

    protected open fun getDefaultPort() = -1


    init {

        val match = pattern.matchEntire(uriString)

        if (match?.groups?.get("authority")?.value.isNullOrBlank()) {
            if (context is CommonURL) {
                userinfo = context.userinfo
                host = context.host
                port = context.port
            } else {
                throw Exception("Can't handle relative uri $uriString without context!")
            }
        } else {
            userinfo = match?.groups?.get("userinfo")?.value ?: ""
            host = match?.groups?.get("host")?.value ?: ""
            val strPort = match?.groups?.get("port")?.value ?: ""
            port = if (strPort.isBlank()) {
                0
            } else {
                strPort.toInt()
            }
        }

        path = URLDecoder.decode(match?.groups?.get("path")?.value ?: "", "UTF-8")
        query = URLDecoder.decode(match?.groups?.get("query")?.value ?: "", "UTF-8")
        fragment = URLDecoder.decode(match?.groups?.get("fragment")?.value ?: "", "UTF-8")
    }

}