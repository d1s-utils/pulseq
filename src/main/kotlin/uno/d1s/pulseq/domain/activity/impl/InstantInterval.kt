/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.domain.activity.impl

import uno.d1s.pulseq.domain.activity.Interval
import uno.d1s.pulseq.util.time.betweenAbs
import java.time.Duration
import java.time.Instant

data class InstantInterval(
    override val duration: Duration, override val start: Instant, override val end: Instant
) : Interval<Instant> {

    companion object {
        fun from(start: Instant, end: Instant) = InstantInterval(betweenAbs(start, end), start, end)
    }
}