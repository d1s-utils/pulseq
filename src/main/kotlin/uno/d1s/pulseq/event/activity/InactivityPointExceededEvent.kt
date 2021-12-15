package uno.d1s.pulseq.event.activity

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.activity.TimeSpan

class InactivityPointExceededEvent(
    source: Any,
    val currentTimeSpan: TimeSpan
) : ApplicationEvent(source)