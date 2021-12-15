package uno.d1s.pulseq.event.device

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Device

@Suppress("UNUSED_PARAMETER")
class DeviceDeletedEvent(
    source: Any,
    device: Device
) : ApplicationEvent(source)