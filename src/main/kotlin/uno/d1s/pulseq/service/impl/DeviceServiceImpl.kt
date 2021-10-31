package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.DeviceService

@Service("deviceService")
class DeviceServiceImpl : DeviceService {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    override fun findAllRegisteredDevices(): List<Device> =
        deviceRepository.findAll()

    override fun findDeviceById(id: String): Device =
        deviceRepository.findById(id).orElseThrow {
            DeviceNotFoundException("Could not find any devices with provided id.")
        }

    override fun findDeviceByName(name: String): Device =
        deviceRepository.findDeviceByNameEqualsIgnoreCase(name).orElseThrow {
            DeviceNotFoundException("Could not find any devices with provided name.")
        }

    override fun findDeviceByIdentify(identify: String): Device = try {
        this.findDeviceById(identify)
    } catch (ex: Exception) {
        try {
            this.findDeviceByName(identify)
        } catch (ex: Exception) {
            throw DeviceNotFoundException("Could not find any devices with provided name nor id.")
        }
    }

    override fun registerNewDevice(name: String): Device =
        deviceRepository.save(Device(name))
}