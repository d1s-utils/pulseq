/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util.http

import uno.d1s.pulseq.util.withSlash

class UrlBuilder(private var initialUrl: String) {

    private var firstParamDeclared = false

    init {
        initialUrl = initialUrl.withSlash()
    }

    fun path(pathVariable: String) {
        initialUrl += if (pathVariable.startsWith("/")) {
            pathVariable.removePrefix("/")
        } else {
            pathVariable
        }.withSlash()
    }

    fun parameter(key: String, value: String) {
        initialUrl += if (firstParamDeclared) {
            "&$key=$value"
        } else {
            "?$key=$value"
        }

        if (!firstParamDeclared) {
            firstParamDeclared = true
        }
    }

    fun build() = initialUrl
}