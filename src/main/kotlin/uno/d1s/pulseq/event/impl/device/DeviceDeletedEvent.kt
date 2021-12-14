package uno.d1s.pulseq.event.impl.device

import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.util.pluralGrammar

class DeviceDeletedEvent(
    source: Any,
    device: Device
) : AbstractNotifiableEvent(
    source, device.let {
        "A device with id `${it.id!!}` and `${it.beats!!.size}` registered beat${pluralGrammar(it.beats!!.size)} were just deleted."
    }
) {
    override val identify = "device-deleted-event"
}