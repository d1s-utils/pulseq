package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.impl.DelayedBeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy
import uno.d1s.pulseq.strategy.device.byAll
import uno.d1s.pulseq.util.findClosestInstantToCurrent

@Service
class BeatServiceImpl : BeatService {

    @Autowired
    private lateinit var beatRepository: BeatRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    // Here we inject ourselves to work with our methods (since we use caching and inject a proxy class)
    @Autowired
    private lateinit var beatService: BeatService

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findBeatById(id: String): Beat = beatRepository.findById(id).orElseThrow {
        BeatNotFoundException()
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheNameConstants.BEAT, CacheNameConstants.BEATS], allEntries = true)
    override fun registerNewBeatWithDeviceIdentify(identify: String): Beat {
        Beat(runCatching {
            deviceService.findDevice(byAll(identify))
        }.getOrElse {
            deviceService.registerNewDevice(identify)
        }, runCatching {
            activityService.getCurrentInactivityDuration()
        }.getOrElse {
            null
        }).let { unsavedBeat ->
            return runCatching {
                if (activityService.isInactivityRelevanceLevelNotCommon()) {
                    beatRepository.save(unsavedBeat).let { savedBeat ->
                        eventPublisher.publishEvent(
                            DelayedBeatReceivedEvent(
                                this, savedBeat
                            )
                        )
                        savedBeat
                    }
                } else {
                    beatRepository.save(unsavedBeat)
                }
            }.getOrElse {
                beatRepository.save(unsavedBeat)
            }
        }
    }

    override fun findAllByDevice(strategy: DeviceFindingStrategy): List<Beat> =
        deviceService.findDevice(strategy).beats!!

    override fun findAllBeats(): List<Beat> = beatRepository.findAll()

    override fun totalBeats(): Int = beatService.findAllBeats().size

    override fun totalBeatsByDevices(): Map<String, Int> = HashMap<String, Int>().apply {
        // It would be easier to use Device's beats DBRef but this will hurt the performance,
        // because this data is not cached (So, I should figure out how to cache it easier)
        beatService.findAllBeats().forEach {
            put(it.device.name, (get(it.device.name) ?: 0) + 1)
        }
    }

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findLastBeat(): Beat = beatService.findAllBeats().let { all ->
        all.firstOrNull { beat ->
            all.map {
                it.beatTime
            }.findClosestInstantToCurrent().orElseThrow {
                NoBeatsReceivedException
            } == beat.beatTime
        } ?: throw NoBeatsReceivedException
    }

    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findFirstBeat(): Beat = beatService.findAllBeats().let { all ->
        all.firstOrNull { beat ->
            all.minOfOrNull {
                it.beatTime
            } == beat.beatTime
        } ?: throw NoBeatsReceivedException
    }
}