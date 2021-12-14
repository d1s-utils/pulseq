package uno.d1s.pulseq.event.impl.device

import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.AbstractNotifiableEvent

class DeviceUpdatedEvent(source: Any, oldDevice: Device, newDevice: Device) : AbstractNotifiableEvent(
    source,
    "A device with id `${oldDevice.id!!}` was just updated. From `${oldDevice}` to ${newDevice}."
) {
    override val identify = "device-updated-event"
}