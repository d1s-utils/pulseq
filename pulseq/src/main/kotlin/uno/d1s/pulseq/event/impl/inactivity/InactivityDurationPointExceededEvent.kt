package uno.d1s.pulseq.event.impl.inactivity

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pretty
import java.time.Duration

open class InactivityDurationPointExceededEvent(
    source: Any,
    val currentInactivity: Duration,
    val inactivityRelevanceLevel: InactivityRelevanceLevel,
    val lastBeat: Beat
) : AbstractNotifiableEvent(source) {

    override val notificationMessage: String
        get() = "**${inactivityRelevanceLevel.nameString} inactivity point was just exceeded.** " +
                "Last beat was registered `${currentInactivity.pretty()}` ago " +
                "from device `${lastBeat.device.name}` with id `${lastBeat.id}`."
}