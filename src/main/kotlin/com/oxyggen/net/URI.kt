package com.oxyggen.net

import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

/*
 * URI type hierarchy:
 *
 *  URI
 *  ├── UnresolvedURI       (partial URI -> no scheme specified)
 *  └── ResolvedURI         (complete URI -> scheme & scheme specific part specified)
 *      ├── MailtoURI (not yet implemented)
 *      └── Context URI
 *          └── URL
 *              └── CommonURL
 *                  ├── HttpURL
 *                  └── FtpURL (not yet implemented)
 */

open class URI(uriString: String, context: ContextURI? = null) {

    val scheme: String
    val schemeSpecificPart: String
    val isRelative: Boolean


    /**
     * From https://en.wikipedia.org/wiki/Uniform_Resource_Identifier
     * URI = scheme:[//authority]path[?query][#fragment]
     * Each URI begins with a scheme name that refers to a specification for assigning identifiers within that scheme.
     * A URI reference is either a URI, or a relative reference when it does not begin with a scheme component
     * followed by a colon (:). A path segment that contains a colon character (e.g., foo:bar) cannot
     * be used as the first path segment of a relative reference if its path component does not begin with a slash (/),
     * as it would be mistaken for a scheme component. Such a path segment must be preceded by a dot path segment (e.g., ./foo:bar).
     */

    init {
        val schemePattern = "^([a-z][a-z0-9+\\-.]*):".toRegex()
        val match = schemePattern.find(uriString)

        val uriStringScheme = match?.value?.substringBefore(':') ?: ""

        // Initialize "is relative" variable (for parsing in subclasses)
        isRelative = uriStringScheme.isBlank()

        scheme = if (!isRelative) {
            uriStringScheme
        } else {
            context?.scheme ?: ""
        }

        schemeSpecificPart = if (uriStringScheme.isNotBlank()) {
            uriString.substring(uriStringScheme.length + 1)
        } else {
            uriString
        }
    }


    companion object {
        val defaultSchemeHandlers = mapOf<String, KClass<out URI>>(
                "http" to HttpURL::class,
                "https" to HttpURL::class,
                "" to UnresolvedURI::class
        )

        /**
         * Parse an URI string and create URI object
         *
         * @param uriString The URI string
         * @param context URI context used when the URI string contains relative URI
         * @return Returns an URI object (or sub-object)
         */
        fun parse(uriString: String, context: ContextURI? = null, schemeHandlers: Map<String, KClass<out URI>> = defaultSchemeHandlers): URI {
            val uri = URI(uriString, context)

            val paramType1 = String::class.createType()
            val paramType2 = ContextURI::class.createType()

            val constructors = schemeHandlers[uri.scheme]?.constructors

            val fullConstructor = constructors?.find {
                it.parameters.size == 2
                        && it.parameters[0].type.isSupertypeOf(paramType1)
                        && it.parameters[1].type.isSupertypeOf(paramType2)
            }

            if (fullConstructor != null) {
                return fullConstructor.call(uriString, context)
            } else {
                val simpleConstructor = constructors?.find {
                    it.parameters.size == 1
                            && it.parameters[0].type.isSupertypeOf(paramType1)
                }

                if (simpleConstructor != null) {
                    return simpleConstructor.call(uriString)
                } else {
                    return uri
                }


            }

        }
    }
}