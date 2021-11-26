package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.exception.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.DeviceService

@Service("deviceService")
class DeviceServiceImpl : DeviceService {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Transactional(readOnly = true)
    override fun findAllRegisteredDevices(): List<Device> =
        deviceRepository.findAll()

    @Transactional(readOnly = true)
    override fun findDeviceById(id: String): Device =
        deviceRepository.findById(id).orElseThrow {
            DeviceNotFoundException("Could not find any devices with provided id.")
        }

    @Transactional(readOnly = true)
    override fun findDeviceByName(name: String): Device =
        deviceRepository.findDeviceByNameEqualsIgnoreCase(name).orElseThrow {
            DeviceNotFoundException("Could not find any devices with provided name.")
        }

    @Transactional(readOnly = true)
    override fun findDeviceByIdentify(identify: String): Device = try {
        this.findDeviceById(identify)
    } catch (ex: Exception) {
        try {
            this.findDeviceByName(identify)
        } catch (ex: Exception) {
            throw DeviceNotFoundException()
        }
    }

    @Transactional
    override fun registerNewDevice(name: String): Device =
        if (deviceRepository.findDeviceByNameEqualsIgnoreCase(name).isPresent) {
            throw DeviceAlreadyExistsException()
        } else {
            deviceRepository.save(Device(name))
        }
}