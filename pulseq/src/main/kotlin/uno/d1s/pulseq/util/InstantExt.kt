package uno.d1s.pulseq.util

import java.time.Instant
import java.util.*

fun List<Instant>.findClosestInstantToCurrent(): Optional<Instant> =
    Optional.ofNullable(
        this.filter {
            Instant.now() >= it
        }.maxOrNull()
    )
