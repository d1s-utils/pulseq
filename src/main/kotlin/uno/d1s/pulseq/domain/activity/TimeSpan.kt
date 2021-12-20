/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.domain.activity

import uno.d1s.pulseq.domain.Beat
import java.time.Duration

data class TimeSpan(
    val duration: Duration,
    val type: TimeSpanType,
    val startBeat: Beat,
    val endBeat: Beat? = null // e.g. for current activity endBeat will be null.
)