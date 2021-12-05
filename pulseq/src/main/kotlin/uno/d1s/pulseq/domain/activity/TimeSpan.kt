package uno.d1s.pulseq.domain.activity

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import java.time.Duration

class TimeSpan(
    val duration: Duration,
    val type: TimeSpanType,
    val inactivityLevel: InactivityRelevanceLevel,
    val startBeat: Beat,
    val endBeat: Beat? = null // e.g. for current activity endBeat will be null.
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeSpan

        if (duration != other.duration) return false
        if (type != other.type) return false
        if (startBeat != other.startBeat) return false
        if (inactivityLevel != other.inactivityLevel) return false
        if (endBeat != other.endBeat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + startBeat.hashCode()
        result = 31 * result + inactivityLevel.hashCode()
        result = 31 * result + (endBeat?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "TimeSpan(duration=$duration, type=$type, startBeat=$startBeat, inactivityLevel=$inactivityLevel, endBeat=$endBeat)"
    }
}