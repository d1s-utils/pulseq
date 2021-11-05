package uno.d1s.pulseq.core.util

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
        }
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