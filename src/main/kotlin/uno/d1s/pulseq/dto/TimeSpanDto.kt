package uno.d1s.pulseq.dto

import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.domain.InactivityRelevanceLevel
import java.time.Duration

data class TimeSpanDto(
    val duration: Duration,
    val type: TimeSpanType,
    val inactivityLevel: InactivityRelevanceLevel,
    val startBeat: String,
    val endBeat: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeSpanDto

        if (duration != other.duration) return false
        if (type != other.type) return false
        if (inactivityLevel != other.inactivityLevel) return false
        if (startBeat != other.startBeat) return false
        if (endBeat != other.endBeat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + inactivityLevel.hashCode()
        result = 31 * result + (startBeat.hashCode())
        result = 31 * result + (endBeat?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TimeSpanDto(duration=$duration, type=$type, inactivityLevel=$inactivityLevel, startBeat=$startBeat, endBeat=$endBeat)"
    }
}