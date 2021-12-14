package uno.d1s.pulseq.event.impl.beat

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.AbstractNotifiableEvent

class BeatDeletedEvent(source: Any, beat: Beat) :
    AbstractNotifiableEvent(source, "A beat with id `${beat.id!!}` was just deleted.") {
    override val identify = "beat-deleted-event"
}