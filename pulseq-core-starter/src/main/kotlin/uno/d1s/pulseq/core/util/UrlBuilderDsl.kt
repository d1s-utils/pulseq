package uno.d1s.pulseq.core.util

inline fun buildUrl(initialUrl: String, block: UrlBuilder.() -> Unit) =
    UrlBuilder(initialUrl).apply(block).build()