package com.oxyggen.net

import kotlin.reflect.KClass

open class URI(uriString: String, context: URI? = null) {

    open val scheme: String
    open val identifier: String

    protected val uriStringIsRelative:Boolean

    init {
        val schemePattern = "^([a-z][a-z0-9+\\-.]*):".toRegex()
        val match = schemePattern.find(uriString)

        val uriStringScheme = match?.value?.substringBefore(':') ?: ""

        // Initialize "is relative" variable (for parsing in subclasses)
        uriStringIsRelative = uriStringScheme.isNullOrBlank()

        scheme = uriStringScheme ?: context?.scheme ?: ""

        identifier = if (uriStringScheme.isNotBlank()) {
            uriString.substring(uriStringScheme.length + 1)
        } else {
            uriString
        }
    }

    /**
     * Get scheme from URI string
     * @param uriString The URI string
     * @return Returns scheme or empty string
     *
     * From https://en.wikipedia.org/wiki/Uniform_Resource_Identifier
     * URI = scheme:[//authority]path[?query][#fragment]
     * Each URI begins with a scheme name that refers to a specification for assigning identifiers within that scheme.
     * A URI reference is either a URI, or a relative reference when it does not begin with a scheme component
     * followed by a colon (:). A path segment that contains a colon character (e.g., foo:bar) cannot
     * be used as the first path segment of a relative reference if its path component does not begin with a slash (/),
     * as it would be mistaken for a scheme component. Such a path segment must be preceded by a dot path segment (e.g., ./foo:bar).
     */
    protected fun getSchemeFromUriString(uriString: String) = if (uriString.contains(':')) {    // Contains scheme
        val testScheme = uriString.split(":", limit = 2).getOrElse(0, { "" }).toLowerCase()
        if (!testScheme.contains('/')) {
            testScheme
        } else {
            ""
        }
    } else {
        ""
    }

    companion object {
        val defaultSchemeHandlers = mapOf<String, KClass<out URI>>(
                "http" to HttpURL::class,
                "https" to HttpURL::class
        )

        /**
         * Parse an URI string and create URI object
         *
         * @param uriString The URI string
         * @param context URI context used when the URI string contains relative URI
         * @return Returns an URI object (or sub-object)
         */
        fun parse(uriString: String, context: URI? = null, schemeHandlers: Map<String, KClass<out URI>> = defaultSchemeHandlers): URI {
            val uri = URI(uriString, context)

            val handler = schemeHandlers[uri.scheme]?.constructors?.first()?.call(uriString, context)
                    ?: uri

            return handler
        }
    }
}