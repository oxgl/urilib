package com.oxyggen.net

open class HttpURL(uriString: String, context: URI? = null) : CommonURL(uriString, context) {
    override fun getDefaultPort(): Int = when (scheme) {
        "https" -> 443
        else -> 80
    }
}