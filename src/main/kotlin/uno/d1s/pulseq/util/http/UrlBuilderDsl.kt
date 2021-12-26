/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import uno.d1s.pulseq.util.http.UrlBuilder

inline fun buildUrl(initialUrl: String, block: UrlBuilder.() -> Unit) =
    UrlBuilder(initialUrl).apply(block).build()