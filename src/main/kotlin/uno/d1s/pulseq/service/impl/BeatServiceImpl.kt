/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.annotation.cache.CacheEvictByIdProvider
import uno.d1s.pulseq.annotation.cache.CachePutByIdProvider
import uno.d1s.pulseq.annotation.cache.CacheableList
import uno.d1s.pulseq.aspect.cache.idProvider.impl.BeatIdProvider
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.domain.activity.impl.InstantInterval
import uno.d1s.pulseq.dto.SimpleTimeSpan
import uno.d1s.pulseq.event.beat.BeatDeletedEvent
import uno.d1s.pulseq.event.beat.BeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.InvalidTimeSpanException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.HolderService
import uno.d1s.pulseq.service.IntervalService
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.DomainFindingStrategy
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import uno.d1s.pulseq.strategy.source.byAll
import java.time.Instant

@Service
class BeatServiceImpl : BeatService {

    @Autowired
    private lateinit var beatRepository: BeatRepository

    @Autowired
    private lateinit var sourceService: SourceService

    @Autowired
    private lateinit var intervalService: IntervalService

    @Autowired
    private lateinit var holderService: HolderService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    // Here we inject ourselves to work with our methods (since we use caching and inject a proxy class)
    @Lazy // used to resolve the circular dependency
    @Autowired
    private lateinit var beatService: BeatService

    @Transactional(readOnly = true)
    @CachePutByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findById(id: String): Beat = beatRepository.findById(id).orElseThrow {
        BeatNotFoundException()
    }

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findFirst(strategy: DomainFindingStrategy): Beat = beatService.findAll(strategy).let { all ->
        all.firstOrNull { beat ->
            all.minOfOrNull {
                it.instant
            } == beat.instant
        } ?: throw NoBeatsReceivedException
    }

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findLast(strategy: DomainFindingStrategy): Beat = beatService.findAll(strategy).let { all ->
        all.firstOrNull { beat ->
            (all.maxOfOrNull {
                it.instant
            } ?: throw NoBeatsReceivedException) == beat.instant
        } ?: throw NoBeatsReceivedException
    }

    @Transactional(readOnly = true)
    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAll(strategy: DomainFindingStrategy): List<Beat> = beatRepository.findAll().filter {
        when (strategy) {
            is DomainFindingStrategy.BySource -> {
                it.source.id!! == strategy.sourceId
            }

            is DomainFindingStrategy.ByHolder -> {
                it.holder.id!! == strategy.holderId
            }

            is DomainFindingStrategy.ByAll -> true
        }
    }.sortedBy {
        it.instant
    }

    @Transactional(readOnly = true)
    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAll(ids: List<String>): List<Beat> = beatRepository.findBeatsByIdIn(ids).orElseThrow {
        BeatNotFoundException()
    }

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAllCreatedIn(timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy): List<Beat> =
        beatService.findAllCreatedInByBeats(timeSpan, beatService.findAll(strategy))

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat> =
        beatService.findAllCreatedInByBeats(timeSpan, beatService.findAll(ids))

    @CacheableList(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun findAllCreatedInByBeats(timeSpan: SimpleTimeSpan, beats: List<Beat>): List<Beat> =
        if (timeSpan.start > timeSpan.end) {
            throw InvalidTimeSpanException()
        } else {
            beats.filter {
                it.instant in timeSpan.start..timeSpan.end
            }
        }

    override fun remove(id: String): Beat = beatService.remove(beatService.findById(id))

    @Transactional
    @CacheEvictByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun remove(beat: Beat): Beat {
        beatRepository.delete(
            beat
        )

        eventPublisher.publishEvent(
            BeatDeletedEvent(
                this, beat
            )
        )

        return beat
    }

    // cache is being evicted by remove() method.
    override fun removeAll(ids: List<String>): List<Beat> {
        val beats = mutableListOf<Beat>()

        ids.forEach {
            val removedBeat = beatService.remove(it)
            beats.add(removedBeat)
        }

        return beats
    }

    override fun removeAllByBeats(beats: List<Beat>): List<Beat> {
        beats.forEach {
            beatService.remove(it)
        }

        return beats;
    }


    override fun removeAll(strategy: DomainFindingStrategy): List<Beat> =
        beatService.removeAllByBeats(beatService.findAll(strategy))

    override fun removeAllCreatedIn(timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy): List<Beat> =
        beatService.removeAllByBeats(beatService.findAllCreatedIn(timeSpan, strategy))

    override fun removeAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat> =
        beatService.removeAllByBeats(beatService.findAllCreatedIn(timeSpan, ids))

    @Transactional
    @CachePutByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun createBeat(identify: String): Beat = beatRepository.save(
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

    @Transactional
    @CachePutByIdProvider(cacheName = CacheNameConstants.BEATS, idProvider = BeatIdProvider::class)
    override fun create(beat: Beat): Beat {
        beatRepository.save(beat)

        eventPublisher.publishEvent(
            BeatReceivedEvent(
                this, beat
            )
        )

        return beat
    }

    override fun create(strategy: SourceFindingStrategy): Beat =
        beatService.create(this.buildBeat(sourceService.findSource(strategy)))

    override fun create(instant: Instant, strategy: SourceFindingStrategy) {
        TODO("Not yet implemented")
    }

    override fun createAll(strategies: List<SourceFindingStrategy>): List<Beat> {
        TODO("Not yet implemented")
    }

    override fun createAll(instants: List<Instant>, strategies: List<SourceFindingStrategy>) {
        TODO("Not yet implemented")
    }

    override fun createAll(strategy: SourceFindingStrategy, count: Long) {
        TODO("Not yet implemented")
    }

    private fun buildBeat(
        source: Source,
        absence: InstantInterval = intervalService.findCurrentAbsenceInterval(
            DomainFindingStrategy.BySource(source.id!!)
        ),
    ) = Beat(source, source.holder, absence.duration)
}