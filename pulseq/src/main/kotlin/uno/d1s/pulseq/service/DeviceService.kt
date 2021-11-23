package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Device

interface DeviceService {

    fun findAllRegisteredDevices(): List<Device>

    fun findDeviceById(id: String): Device

    fun findDeviceByName(name: String): Device

    fun findDeviceByIdentify(identify: String): Device

    fun registerNewDevice(name: String): Device
}