/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto.beat

import java.time.Duration
import java.time.Instant

class BeatDto(
    val source: String,
    val beatTime: Instant,
    val inactivityBeforeBeat: Duration?
) {
    var id: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeatDto

        if (source != other.source) return false
        if (beatTime != other.beatTime) return false
        if (inactivityBeforeBeat != other.inactivityBeforeBeat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + beatTime.hashCode()
        result = 31 * result + (inactivityBeforeBeat?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BeatDto(source='$source', beatTime=$beatTime, inactivityBeforeBeat=$inactivityBeforeBeat, id=$id)"
    }
}