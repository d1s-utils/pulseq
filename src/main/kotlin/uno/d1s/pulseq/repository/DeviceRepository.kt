package uno.d1s.pulseq.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.domain.Device
import java.util.*

@Repository
interface DeviceRepository : MongoRepository<Device, String> {

    fun findDeviceByNameEqualsIgnoreCase(deviceName: String): Optional<Device>
}