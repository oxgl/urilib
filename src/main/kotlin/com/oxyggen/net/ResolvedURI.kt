package com.oxyggen.net

abstract class ResolvedURI(uriString: String, context: ContextURI? = null) : URI(uriString, context) {
    abstract fun toResolvedUriString():String
}