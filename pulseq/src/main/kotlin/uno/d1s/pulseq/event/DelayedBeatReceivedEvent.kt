package uno.d1s.pulseq.event

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Beat

class DelayedBeatReceivedEvent(
    source: Any,
    val beat: Beat
) : ApplicationEvent(source)