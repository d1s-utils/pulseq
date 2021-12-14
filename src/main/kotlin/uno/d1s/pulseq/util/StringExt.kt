package uno.d1s.pulseq.util

import org.springframework.util.StringUtils

fun String.pluralGrammar(number: Number) =
    if (number != 1) "${this}s" else this

fun pluralGrammar(number: Number) =
    if (number != 1) "s" else ""

fun String.withSlash() =
    if (!this.endsWith("/")) {
        "$this/"
    } else {
        this
    }

fun String.replacePathPlaceholder(placeholder: String, replacement: String) =
    this.replace("{$placeholder}", replacement)

fun String.fromCommaSeparatedString(): List<String> = StringUtils.delimitedListToStringArray(this, ",").asList()