package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.util.grammar

@Component
class DevicesStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var deviceService: DeviceService

    override val title: String
        get() = "Devices"

    override val description: String
        get() = deviceService.findAllRegisteredDevices().let { devices ->
            if (devices.isEmpty()) "No devices registered yet." else
                "The server receives beats from ${devices.size} device${grammar(devices.size)}: " +
                        devices.map {
                            it.name
                        }.toString().removePrefix("[").removeSuffix("]")
        }
}