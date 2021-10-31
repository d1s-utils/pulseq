package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.configuration.property.ServerConfigurationProperties
import uno.d1s.pulseq.configuration.property.model.InactivityDurationPointConfigurationModel
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
    private lateinit var serverConfigurationProperties: ServerConfigurationProperties

    @Transactional(readOnly = true)
    override fun getCurrentInactivity(): Duration = Instant.now().let { now ->
        Duration.between(
            beatService.findLastBeat().beatTime,
            now
        ).abs()
    }

    @Transactional(readOnly = true)
    override fun getLongestInactivity(): Duration =
        beatService.findAllBeats().map {
            it.inactivityBeforeBeat
        }.maxOrNull() ?: throw NoBeatsReceivedException

    @Transactional(readOnly = true)
    override fun getCurrentInactivityPretty(): String =
        this.getCurrentInactivity().pretty()

    @Transactional(readOnly = true)
    override fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel =
        if (this.isLongInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.LONG
        } else if (this.isWarningInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.WARNING
        } else {
            InactivityRelevanceLevel.COMMON
        }

    @Transactional(readOnly = true)
    override fun isRelevanceLevelNotCommon(): Boolean =
        this.getCurrentInactivityRelevanceLevel() != InactivityRelevanceLevel.COMMON

    @Transactional(readOnly = true)
    private fun isLongInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(serverConfigurationProperties.inactivity)

    @Transactional(readOnly = true)
    private fun isWarningInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(serverConfigurationProperties.warningInactivity)

    @Transactional(readOnly = true)
    private fun isDurationPointExceeded(inactivityDuration: InactivityDurationPointConfigurationModel) = try {
        this.getCurrentInactivity() > Duration.of(inactivityDuration.value, inactivityDuration.unit)
    } catch (_: NoBeatsReceivedException) {
        false
    }
}