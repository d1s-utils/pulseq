package uno.d1s.pulseq.event.device

import org.springframework.context.ApplicationEvent
import uno.d1s.pulseq.domain.Device

@Suppress("UNUSED_PARAMETER")
class DeviceUpdatedEvent(source: Any, oldDevice: Device, newDevice: Device) : ApplicationEvent(source)