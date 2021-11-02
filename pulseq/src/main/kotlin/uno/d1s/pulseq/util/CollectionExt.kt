package uno.d1s.pulseq.util

import org.springframework.util.StringUtils

fun Collection<*>.toCommaDelimitedString() =
    StringUtils.collectionToDelimitedString(this, ", ")