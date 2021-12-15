package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.device.DeviceDeletedEvent
import uno.d1s.pulseq.event.device.DeviceUpdatedEvent
import uno.d1s.pulseq.exception.impl.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.impl.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy.*
import uno.d1s.pulseq.strategy.device.byName

@Service
class DeviceServiceImpl : DeviceService {

    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Transactional(readOnly = true)
    override fun findAllRegisteredDevices(): List<Device> = deviceRepository.findAll()

    @Transactional(readOnly = true)
    override fun findDevice(strategy: DeviceFindingStrategy): Device = when (strategy) {
        is ById -> this.findById(strategy.identify)
        is ByName -> this.findByName(strategy.identify)
        is ByAll -> try {
            this.findById(strategy.identify)
        } catch (_: DeviceNotFoundException) {
            try {
                this.findByName(strategy.identify)
            } catch (_: DeviceNotFoundException) {
                throw DeviceNotFoundException("Could not find any device with provided name or id.")
            }
        }
    }.apply {
        beats = beatService.findAllByDevice(strategy)
    }

    @Transactional
    override fun registerNewDevice(name: String): Device =
        if (deviceRepository.findDeviceByNameEqualsIgnoreCase(name).isPresent) {
            throw DeviceAlreadyExistsException()
        } else {
            deviceRepository.save(Device(name))
        }

    @Transactional
    override fun updateDevice(strategy: DeviceFindingStrategy, device: Device): Device {
        val exisingDevice =
            this.findDevice(strategy)

        try {
            this.findDevice(byName(device.name))
            throw DeviceAlreadyExistsException()
        } catch (_: DeviceNotFoundException) {
            return deviceRepository.save(Device(device.name).apply {
                id = exisingDevice.id
                beats = exisingDevice.beats
            }).also {
                applicationEventPublisher.publishEvent(
                    DeviceUpdatedEvent(this, exisingDevice, it)
                )
            }
        }
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheNameConstants.BEAT, CacheNameConstants.BEATS], allEntries = true)
    override fun deleteDevice(strategy: DeviceFindingStrategy) {
        val device = this.findDevice(strategy)

        beatService.findAllBeats().forEach {
            if (it.device == device) {
                beatService.deleteBeat(it.id!!, false)
            }
        }

        deviceRepository.delete(
            device
        )

        applicationEventPublisher.publishEvent(
            DeviceDeletedEvent(
                this, device
            )
        )
    }

    @Transactional(readOnly = true)
    override fun findDeviceBeats(strategy: DeviceFindingStrategy): List<Beat> = this.findDevice(strategy).beats!!

    private fun findById(id: String) = deviceRepository.findById(id).orElseThrow {
        DeviceNotFoundException("Could not find any device with provided id.")
    }

    private fun findByName(name: String) = deviceRepository.findDeviceByNameEqualsIgnoreCase(name).orElseThrow {
        DeviceNotFoundException("Could not find any device with provided name.")
    }
}