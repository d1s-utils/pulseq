package uno.d1s.pulseq.event.impl.beat

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pretty

class BeatReceivedEvent(
    source: Any,
    beat: Beat,
    delayed: Boolean
) : AbstractNotifiableEvent(source, beat.let {
    "A ${
        if (delayed) {
            "delayed "
        } else {
            ""
        }
    }beat with id `${beat.id}` was just received ${
        if (it.inactivityBeforeBeat != null) {
            "after `${it.inactivityBeforeBeat.pretty()}` of inactivity "
        } else {
            ""
        }
    }from device `${beat.device.name}`."
}) {
    override val identify = "beat-received-event"
}