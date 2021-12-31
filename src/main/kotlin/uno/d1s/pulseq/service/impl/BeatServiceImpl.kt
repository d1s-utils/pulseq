/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.annotation.cache.CachePutByIdProvider
import uno.d1s.pulseq.annotation.cache.CacheableList
import uno.d1s.pulseq.aspect.cache.idProvider.impl.BeatIdProvider
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.beat.BeatDeletedEvent
import uno.d1s.pulseq.event.beat.BeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.IntervalService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.byAll
import uno.d1s.pulseq.util.time.findClosestInstantToCurrent

@Service
class BeatServiceImpl : BeatService {

    @Autowired
    private lateinit var beatRepository: BeatRepository

    @Autowired
    private lateinit var sourceService: SourceService

    @Autowired
    private lateinit var intervalService: IntervalService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    // Here we inject ourselves to work with our methods (since we use caching and inject a proxy class)
    @Lazy // used to resolve the circular dependency
    @Autowired
    private lateinit var beatService: BeatService

    @Transactional(readOnly = true)
    @CachePutByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findBeatById(id: String): Beat = beatRepository.findById(id).orElseThrow {
        BeatNotFoundException()
    }

    @Transactional
    @CachePutByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun createBeat(identify: String): Beat =
        beatRepository.save(
            Beat(runCatching {
                sourceService.findSource(byAll(identify))
            }.getOrElse {
                it.printStackTrace()
                sourceService.registerNewSource(identify)
            }, runCatching {
                intervalService.findCurrentAbsenceInterval()
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

    @Transactional(readOnly = true)
    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAllBeats(): List<Beat> = beatRepository.findAll().sortedBy {
        it.instant
    }

    override fun totalBeats(): Int = beatService.findAll().size

    override fun totalBeatsBySources(): Map<String, Int> = HashMap<String, Int>().apply {
        // It would be easier to use Source's beats DBRef but this will hurt the performance,
        // because this data is not cached (So, I should figure out how to cache it easier)
        beatService.findAll().forEach {
            put(it.source.name, (get(it.source.name) ?: 0) + 1)
        }
    }

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findLast(): Beat = beatService.findAll().let { all ->
        all.firstOrNull { beat ->
            all.map {
                it.instant
            }.findClosestInstantToCurrent().orElseThrow {
                NoBeatsReceivedException
            } == beat.instant
        } ?: throw NoBeatsReceivedException
    }

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findFirst(): Beat = beatService.findAll().let { all ->
        all.firstOrNull { beat ->
            all.minOfOrNull {
                it.instant
            } == beat.instant
        } ?: throw NoBeatsReceivedException
    }

    @Transactional
    override fun remove(id: String, sendEvent: Boolean) {
        val beatForRemoval = beatService.findById(id)

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