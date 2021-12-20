package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.beat.BeatDeletedEvent
import uno.d1s.pulseq.event.beat.BeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import uno.d1s.pulseq.strategy.source.byAll
import uno.d1s.pulseq.util.findClosestInstantToCurrent

@Service
class BeatServiceImpl : BeatService {

    @Autowired
    private lateinit var beatRepository: BeatRepository

    @Autowired
    private lateinit var sourceService: SourceService

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    // Here we inject ourselves to work with our methods (since we use caching and inject a proxy class)
    @Lazy // used to resolve the circular dependency
    @Autowired
    private lateinit var beatService: BeatService

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = [CacheNameConstants.BEAT])
    override fun findBeatById(id: String): Beat = beatRepository.findById(id).orElseThrow {
        BeatNotFoundException()
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheNameConstants.BEAT, CacheNameConstants.BEATS], allEntries = true)
    override fun registerNewBeatWithSourceIdentify(identify: String): Beat =
        beatRepository.save(
            Beat(runCatching {
                sourceService.findSource(byAll(identify))
            }.getOrElse {
                it.printStackTrace()
                sourceService.registerNewSource(identify)
            }, runCatching {
                activityService.getCurrentInactivityDuration()
            }.getOrElse {
                null
            })
        ).also {
            eventPublisher.publishEvent(
                BeatReceivedEvent(
                    this, it
                )
            )
        }

    // I dont know why do I keep this here... I'll just TODO it.
    override fun findAllBySource(strategy: SourceFindingStrategy): List<Beat> = sourceService.findSourceBeats(strategy)

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = [CacheNameConstants.BEATS])
    override fun findAllBeats(): List<Beat> = beatRepository.findAll().sortedBy {
        it.beatTime
    }

    override fun totalBeats(): Int = beatService.findAllBeats().size

    override fun totalBeatsBySources(): Map<String, Int> = HashMap<String, Int>().apply {
        // It would be easier to use Source's beats DBRef but this will hurt the performance,
        // because this data is not cached (So, I should figure out how to cache it easier)
        beatService.findAllBeats().forEach {
            put(it.source.name, (get(it.source.name) ?: 0) + 1)
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

    override fun deleteBeat(id: String, sendEvent: Boolean) {
        val beatForRemoval = beatService.findBeatById(id)

        beatRepository.delete(
            beatForRemoval
        )

        if (sendEvent) {
            eventPublisher.publishEvent(
                BeatDeletedEvent(
                    this, beatForRemoval
                )
            )
        }
    }
}