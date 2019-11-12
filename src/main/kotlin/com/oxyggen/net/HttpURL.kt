package com.oxyggen.net

open class HttpURL(uriString: String, context: ContextURI? = null) : CommonURL(uriString, context) {
    override fun getDefaultPort(): Int = when (scheme) {
        "https" -> 443
        else -> 80
    }

}