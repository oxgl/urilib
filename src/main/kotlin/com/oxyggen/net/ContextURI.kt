package com.oxyggen.net

open class ContextURI(uriString: String, uri: ContextURI?) : ResolvedURI(uriString, uri) {
    open fun resolve(uri: URI) = (if (uri is ResolvedURI) {
        uri
    } else if (uri is UnresolvedURI) {
        parse(uriString = uri.schemeSpecificPart, context = this)
    } else {
        this
    }) as ResolvedURI
}
