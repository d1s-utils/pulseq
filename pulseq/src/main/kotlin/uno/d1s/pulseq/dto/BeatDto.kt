package uno.d1s.pulseq.dto

import java.time.Duration
import java.time.Instant

class BeatDto(
    val device: String,
    val beatTime: Instant,
    val inactivityBeforeBeat: Duration?
) {
    var id: String? = null
}