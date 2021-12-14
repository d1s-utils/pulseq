package uno.d1s.pulseq.event.impl

import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pretty

class InactivityPointExceededEvent(
    source: Any,
    val currentTimeSpan: TimeSpan
) : AbstractNotifiableEvent(
    source, "${currentTimeSpan.inactivityLevel.nameString} inactivity point was just exceeded. " +
            "Last beat was registered `${currentTimeSpan.duration.pretty()}` ago " +
            "from device `${currentTimeSpan.startBeat.device.name}` with id `${currentTimeSpan.startBeat.id}`."
) {
    override val identify = "inactivity-point-exceeded-event"
}