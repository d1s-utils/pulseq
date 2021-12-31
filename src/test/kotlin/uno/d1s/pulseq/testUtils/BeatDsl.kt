/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import java.time.Duration
import java.time.Instant

inline fun buildBeat(builder: BeatBuilder.() -> Unit) =
    BeatBuilder().apply(builder).build()

class BeatBuilder(
    var source: Source = testBeat.source,
    var inactivityBeforeBeat: Duration? = testBeat.inactivityBeforeBeat,
    var beatTime: Instant = testBeat.instant,
    var id: String? = testBeat.id
) {

    fun build() = Beat(source, inactivityBeforeBeat, beatTime).apply {
        id = this@BeatBuilder.id
    }
}