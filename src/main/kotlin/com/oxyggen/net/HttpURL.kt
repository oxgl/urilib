package com.oxyggen.net

open class HttpURL(uriString: String, context: ContextURI? = null) : CommonURL(uriString, context) {
    override fun getDefaultPort(): Int = when (scheme) {
        "https" -> 443
        else -> 80
    }

    open val queryParam: Map<String, List<String>> by lazy {
        val result = mutableMapOf<String, MutableList<String>>()

        if (query.isNotBlank()) {
            val paramValue = query.split('&')
            paramValue.forEach {
                val parts = it.split('=')
                val name = percentDecode(parts[0])
                val value = percentDecode(parts.getOrElse(1) { "" })

                if (result.containsKey(name))
                    result[name]!!.add(value)
                else
                    result.put(name, mutableListOf(value))
            }
        }
        result
    }

}