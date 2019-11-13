package com.oxyggen.net

class MailtoURI(uriString: String, context: ContextURI? = null) : ResolvedURI(uriString, context) {
    override fun toResolvedUriString(): String = toUriString()
}