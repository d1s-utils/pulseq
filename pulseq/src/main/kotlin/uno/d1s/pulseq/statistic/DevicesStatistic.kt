package uno.d1s.pulseq.statistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.util.grammar

@Component
class DevicesStatistic : Statistic {

    @Autowired
    private lateinit var deviceService: DeviceService

    override val identify = "devices"

    override val title = "Devices"

    override val description
        get() = deviceService.findAllRegisteredDevices().let { devices ->
            if (devices.isEmpty()) "No devices registered yet." else
                "The server receives beats from ${devices.size} device${grammar(devices.size)}: " +
                        StringUtils.collectionToCommaDelimitedString(devices)
        }

    override val shortDescription =
        StringUtils.collectionToCommaDelimitedString(deviceService.findAllRegisteredDevices())
}