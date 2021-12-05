package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.exception.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy.*

@Service
class DeviceServiceImpl : DeviceService {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Transactional(readOnly = true)
    override fun findAllRegisteredDevices(): List<Device> = deviceRepository.findAll()

    @Transactional(readOnly = true)
    override fun findDevice(strategy: DeviceFindingStrategy): Device = when (strategy) {
        is ById -> deviceRepository.findById(strategy.identify).orElseThrow {
            DeviceNotFoundException("Could not find any device with provided id.")
        }
        is ByName -> deviceRepository.findDeviceByNameEqualsIgnoreCase(strategy.identify).orElseThrow {
            DeviceNotFoundException("Could not find any device with provided name.")
        }
        is ByAll -> deviceRepository.findDeviceByNameEqualsIgnoreCaseOrIdEquals(strategy.identify).orElseThrow {
            DeviceNotFoundException("Could not find any device with provided name or id.")
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