package uno.d1s.pulseq.event

import org.springframework.context.ApplicationEvent

abstract class AbstractNotifiableEvent(source: Any) : NotifiableEvent, ApplicationEvent(source)