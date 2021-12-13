package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy

interface DeviceService {

    fun findAllRegisteredDevices(): List<Device>

    fun findDevice(
        strategy: DeviceFindingStrategy
    ): Device

    fun registerNewDevice(name: String): Device

    fun updateDevice(strategy: DeviceFindingStrategy, device: Device): Device

    fun deleteDevice(strategy: DeviceFindingStrategy)

    fun findDeviceBeats(strategy: DeviceFindingStrategy): List<Beat>
}