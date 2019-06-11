package com.oxyggen.net

import kotlin.reflect.KClass

open class URI {
    companion object {
        val defaultSchemeHandlers = mapOf<String, KClass<out URI>>(
                "http" to HttpURL::class,
                "https" to HttpURL::class,
                "" to RelativeURI::class
        )

        private val handlers: MutableMap<String, URI>? = null

        fun parse(uri: String, schemeHandlers: Map<String, KClass<out URI>> = defaultSchemeHandlers): URI {
            var scheme = ""
            var identifier = ""
            if (uri.contains(':')) {    // Contains scheme
                val parts = uri.split(":", limit = 2)
                scheme = parts.getOrElse(0, { "" }).toLowerCase()
                identifier = parts.getOrElse(0, { "" })
            } else {
                identifier = uri
            }

            val handlerClass = schemeHandlers[scheme]
            val handler = if (handlerClass != null) {
                handlerClass.constructors.first().call()
            } else {
                URI()
            }

            return handler

        }
    }
}