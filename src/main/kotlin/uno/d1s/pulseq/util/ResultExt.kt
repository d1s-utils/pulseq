/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

fun Result<String>.getOrMessage(): String = this.getOrElse {
    it.message ?: "An error occurred, message is not available."
}