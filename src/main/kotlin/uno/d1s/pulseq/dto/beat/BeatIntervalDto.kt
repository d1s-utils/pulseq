/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto.beat

import uno.d1s.pulseq.domain.activity.IntervalType
import java.time.Duration

data class BeatIntervalDto(
    val duration: Duration,
    val type: IntervalType,
    val startBeat: String,
    val endBeat: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeatIntervalDto

        if (duration != other.duration) return false
        if (type != other.type) return false
        if (startBeat != other.startBeat) return false
        if (endBeat != other.endBeat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (startBeat.hashCode())
        result = 31 * result + (endBeat?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "IntervalDto(duration=$duration, type=$type, startBeat=$startBeat, endBeat=$endBeat)"
    }
}