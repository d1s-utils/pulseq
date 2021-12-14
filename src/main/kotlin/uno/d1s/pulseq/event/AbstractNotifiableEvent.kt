package uno.d1s.pulseq.event

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.util.Identifiable

abstract class AbstractNotifiableEvent(
    source: Any,
    @JsonIgnore override val notificationMessage: String
) :
    NotifiableEvent, Identifiable, ApplicationEvent(source)