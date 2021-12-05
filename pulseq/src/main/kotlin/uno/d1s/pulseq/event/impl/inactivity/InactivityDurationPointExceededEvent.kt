package uno.d1s.pulseq.event.impl.inactivity

import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pretty

open class InactivityDurationPointExceededEvent(
    source: Any,
    val currentTimeSpan: TimeSpan
) : AbstractNotifiableEvent(source) {

    override val notificationMessage: String
        get() = "**${currentTimeSpan.inactivityLevel.nameString} inactivity point was just exceeded.** " +
                "Last beat was registered `${currentTimeSpan.duration.pretty()}` ago " +
                "from device `${currentTimeSpan.startBeat.device.name}` with id `${currentTimeSpan.startBeat.id}`."
}