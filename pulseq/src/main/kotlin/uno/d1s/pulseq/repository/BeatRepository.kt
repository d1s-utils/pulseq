package uno.d1s.pulseq.repository

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import java.util.*

@Repository
interface BeatRepository : MongoRepository<Beat, String> {

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    fun findAllByDeviceIdEquals(id: String): List<Beat>

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    fun findAllByDeviceNameEqualsIgnoreCase(deviceName: String): List<Beat>

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findById(id: String): Optional<Beat>

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findAll(): MutableList<Beat>
}