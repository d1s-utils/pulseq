package uno.d1s.pulseq.event.beat

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Beat

@Suppress("UNUSED_PARAMETER")
class BeatReceivedEvent(
    source: Any,
    beat: Beat
) : ApplicationEvent(source)