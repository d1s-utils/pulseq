package uno.d1s.pulseq.statistic.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.core.util.grammar
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.util.toCommaDelimitedString

@Component
class DevicesStatistic : Statistic {

    companion object {
        private const val EMPTY_COLLECTION_MESSAGE = "No devices registered yet."
    }

    @Autowired
    private lateinit var deviceService: DeviceService

    override val identify = "devices"

    override val title = "Devices"

    override val description
        get() = deviceService.findAllRegisteredDevices().let { devices ->
            if (devices.isEmpty()) {
                return@let EMPTY_COLLECTION_MESSAGE
            }

            "The server receives beats from ${devices.size} device${grammar(devices.size)}: " +
                    devices.map {
                        it.name
                    }.toCommaDelimitedString()
        }

    override val shortDescription
        get() = deviceService.findAllRegisteredDevices().map {
            it.name
        }.toCommaDelimitedString(EMPTY_COLLECTION_MESSAGE)
}