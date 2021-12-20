package uno.d1s.pulseq.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

val currentRequest get() = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request