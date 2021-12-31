/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.domain.activity.impl

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.domain.activity.TypedInterval
import uno.d1s.pulseq.util.time.betweenAbs
import java.time.Duration
import java.time.Instant

data class BeatInterval(
    override val duration: Duration,
    override val start: Beat,
    override val end: Beat? = null, // e.g. for current activity endBeat will be null.
    override val type: IntervalType
) : TypedInterval<Beat?> {

    companion object {
        fun from(start: Beat, end: Beat?, type: IntervalType): BeatInterval =
            BeatInterval(betweenAbs(start.instant, end?.instant ?: Instant.now()), start, end, type)
    }
}