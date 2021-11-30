package uno.d1s.pulseq.util

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import java.time.Duration
import java.time.Instant

inline fun buildBeat(builder: BeatBuilder.() -> Unit) =
    BeatBuilder().apply(builder).build()

class BeatBuilder(
    var device: Device = testBeat.device,
    var inactivityBeforeBeat: Duration? = testBeat.inactivityBeforeBeat,
    var beatTime: Instant = testBeat.beatTime,
    var id: String? = testBeat.id
) {

    fun build() = Beat(device, inactivityBeforeBeat, beatTime).apply {
        id = this@BeatBuilder.id
    }
}