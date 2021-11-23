package uno.d1s.pulseq.util

fun Result<String>.getOrMessage(): String = this.getOrElse {
    it.message ?: "An error occurred, message is not available."
}