package uno.d1s.pulseq.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.domain.Beat

@Repository
interface BeatRepository : MongoRepository<Beat, String> {

    fun findAllByDeviceIdEquals(id: String): List<Beat>

    fun findAllByDeviceNameEqualsIgnoreCase(deviceName: String): List<Beat>

    fun findAllByDeviceNameEqualsIgnoreCaseOrDeviceIdEquals(identify: String): List<Beat>
}