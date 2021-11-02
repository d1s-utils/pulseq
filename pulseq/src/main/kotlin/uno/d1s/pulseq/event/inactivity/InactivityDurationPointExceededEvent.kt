package uno.d1s.pulseq.event.inactivity

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Beat
import java.time.Duration

open class InactivityDurationPointExceededEvent(
    source: Any,
    val currentInactivity: Duration,
    val inactivityRelevanceLevel: InactivityRelevanceLevel,
    val lastBeat: Beat
) : ApplicationEvent(source)