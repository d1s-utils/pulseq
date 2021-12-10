package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.core.util.pluralGrammar
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.toCommaDelimitedString

@Component
class DevicesMetric : Metric {

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

            "The server receives beats from ${devices.size} device${pluralGrammar(devices.size)}: " +
                    devices.map {
                        it.name
                    }.toCommaDelimitedString()
        }

    override val shortDescription
        get() = deviceService.findAllRegisteredDevices().map {
            it.name
        }.toCommaDelimitedString(EMPTY_COLLECTION_MESSAGE)
}