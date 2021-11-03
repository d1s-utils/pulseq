package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.configuration.property.InactivityConfigurationProperties
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.InactivityStatusService
import uno.d1s.pulseq.util.pretty
import java.time.Duration
import java.time.Instant

@Service("inactivityStatusService")
class InactivityStatusServiceImpl : InactivityStatusService {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var inactivityConfigurationProperties: InactivityConfigurationProperties

    @Transactional(readOnly = true)
    override fun getCurrentInactivity(): Duration = Instant.now().let { now ->
        Duration.between(
            beatService.findLastBeat().beatTime,
            now
        ).abs()
    }

    @Transactional(readOnly = true)
    override fun getLongestInactivity(): Duration =
        beatService.findAllBeats().filter {
            it.inactivityBeforeBeat != null
        }.map {
            it.inactivityBeforeBeat!! // filtered
        }.maxOrNull()?.let { inactivity ->
            val current = this.getCurrentInactivity()

            if (inactivity < current) {
                current
            } else {
                inactivity
            }
        } ?: throw NoBeatsReceivedException

    @Transactional(readOnly = true)
    override fun getCurrentInactivityPretty(): String =
        this.getCurrentInactivity().pretty()

    override fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel =
        if (this.isLongInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.LONG
        } else if (this.isWarningInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.WARNING
        } else {
            InactivityRelevanceLevel.COMMON
        }

    override fun isRelevanceLevelNotCommon(): Boolean =
        this.getCurrentInactivityRelevanceLevel() != InactivityRelevanceLevel.COMMON

    private fun isLongInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(inactivityConfigurationProperties.common)

    private fun isWarningInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(inactivityConfigurationProperties.warning)

    private fun isDurationPointExceeded(millis: Long) = try {
        this.getCurrentInactivity() > Duration.ofMillis(millis)
    } catch (_: NoBeatsReceivedException) {
        false
    }
}