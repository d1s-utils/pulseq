package uno.d1s.pulseq.event.impl

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pretty

class DelayedBeatReceivedEvent(
    source: Any,
    val beat: Beat
) : AbstractNotifiableEvent(source) {

    override val notificationMessage: String
        get() = beat.inactivityBeforeBeat.let {
            "A beat with id `${beat.id}` was just received" + if (it == null) {
                "!"
            } else {
                " after `${it.pretty()}` of inactivity!"
            }
        }
}