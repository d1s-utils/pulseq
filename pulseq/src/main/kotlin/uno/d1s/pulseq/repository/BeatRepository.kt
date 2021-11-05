package uno.d1s.pulseq.repository

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import java.util.*

@Repository
@CacheConfig(cacheNames = [CacheNameConstants.BEAT])
interface BeatRepository : MongoRepository<Beat, String> {

    @Cacheable
    fun findAllByDeviceIdEquals(id: String): List<Beat>

    @Cacheable
    fun findAllByDeviceNameEqualsIgnoreCase(deviceName: String): List<Beat>

    @Cacheable
    override fun findById(id: String): Optional<Beat>

    override fun findAll(): MutableList<Beat>
}