package uno.d1s.pulseq.event.source

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Source

@Suppress("UNUSED_PARAMETER")
class SourceDeletedEvent(
    sourceClass: Any,
    source: Source
) : ApplicationEvent(sourceClass)