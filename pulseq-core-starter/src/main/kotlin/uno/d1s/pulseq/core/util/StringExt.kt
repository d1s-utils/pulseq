package uno.d1s.pulseq.core.util

fun String.grammar(number: Number) =
    if (number != 1) "${this}s" else this

fun grammar(number: Number) =
    if (number != 1) "s" else ""

fun String.withSlash() =
    if (!this.endsWith("/")) {
        "$this/"
    } else {
        this
    }

fun String.replacePathPlaceholder(placeholder: String, replacement: String) =
    this.replace("{$placeholder}", replacement)