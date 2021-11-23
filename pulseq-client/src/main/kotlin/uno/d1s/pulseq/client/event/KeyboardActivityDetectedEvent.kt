package uno.d1s.pulseq.client.event

import org.springframework.context.ApplicationEvent

class KeyboardActivityDetectedEvent(source: Any, val key: Char) : ApplicationEvent(source)