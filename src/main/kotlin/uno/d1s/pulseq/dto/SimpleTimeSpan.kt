/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto

import java.time.Instant

data class SimpleTimeSpan(
    val start: Instant, val end: Instant
)