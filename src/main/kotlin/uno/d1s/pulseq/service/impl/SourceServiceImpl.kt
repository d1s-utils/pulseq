package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.constant.cache.CacheNameConstants
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.event.source.SourceDeletedEvent
import uno.d1s.pulseq.event.source.SourceUpdatedEvent
import uno.d1s.pulseq.exception.impl.SourceAlreadyExistsException
import uno.d1s.pulseq.exception.impl.SourceNotFoundException
import uno.d1s.pulseq.repository.SourceRepository
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy.*
import uno.d1s.pulseq.strategy.source.byName

@Service
class SourceServiceImpl : SourceService {

    @Autowired
    private lateinit var sourceRepository: SourceRepository

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Transactional(readOnly = true)
    override fun findAllRegisteredSources(): List<Source> = sourceRepository.findAll()

    @Transactional(readOnly = true)
    override fun findSource(strategy: SourceFindingStrategy): Source = when (strategy) {
        is ById -> this.findById(strategy.identify)
        is ByName -> this.findByName(strategy.identify)
        is ByAll -> try {
            this.findById(strategy.identify)
        } catch (_: SourceNotFoundException) {
            try {
                this.findByName(strategy.identify)
            } catch (_: SourceNotFoundException) {
                throw SourceNotFoundException("Could not find any source with provided name or id.")
            }
        }
    }.apply {
        beats = beatService.findAllBySource(strategy)
    }

    @Transactional
    override fun registerNewSource(name: String): Source =
        if (sourceRepository.findSourceByNameEqualsIgnoreCase(name).isPresent) {
            throw SourceAlreadyExistsException()
        } else {
            sourceRepository.save(Source(name))
        }

    @Transactional
    override fun updateSource(strategy: SourceFindingStrategy, source: Source): Source {
        val exisingSource =
            this.findSource(strategy)

        try {
            this.findSource(byName(source.name))
            throw SourceAlreadyExistsException()
        } catch (_: SourceNotFoundException) {
            return sourceRepository.save(Source(source.name).apply {
                id = exisingSource.id
                beats = exisingSource.beats
            }).also {
                applicationEventPublisher.publishEvent(
                    SourceUpdatedEvent(this, exisingSource, it)
                )
            }
        }
    }

    @Transactional
    @CacheEvict(cacheNames = [CacheNameConstants.BEAT, CacheNameConstants.BEATS], allEntries = true)
    override fun deleteSource(strategy: SourceFindingStrategy) {
        val source = this.findSource(strategy)

        beatService.findAllBeats().forEach {
            if (it.source == source) {
                beatService.deleteBeat(it.id!!, false)
            }
        }

        sourceRepository.delete(
            source
        )

        applicationEventPublisher.publishEvent(
            SourceDeletedEvent(
                this, source
            )
        )
    }

    @Transactional(readOnly = true)
    override fun findSourceBeats(strategy: SourceFindingStrategy): List<Beat> = this.findSource(strategy).beats!!

    private fun findById(id: String) = sourceRepository.findById(id).orElseThrow {
        SourceNotFoundException("Could not find any source with provided id.")
    }

    private fun findByName(name: String) = sourceRepository.findSourceByNameEqualsIgnoreCase(name).orElseThrow {
        SourceNotFoundException("Could not find any source with provided name.")
    }
}