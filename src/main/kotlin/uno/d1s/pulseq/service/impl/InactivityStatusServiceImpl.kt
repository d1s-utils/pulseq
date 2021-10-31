package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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

    override fun getCurrentInactivity(): Duration = Instant.now().let { now ->
        Duration.between(
            beatService.findLastBeat().beatTime,
            now
        ).abs()
    }

    override fun getLongestInactivity(): Duration =
        beatService.findAllBeats().map {
            it.inactivityBeforeBeat
        }.maxOrNull() ?: throw NoBeatsReceivedException

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
        this.isDurationPointExceeded(serverConfigurationProperties.inactivity)

    private fun isWarningInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(serverConfigurationProperties.warningInactivity)

    private fun isDurationPointExceeded(inactivityDuration: InactivityDurationPointConfigurationModel) = try {
        this.getCurrentInactivity() > Duration.of(inactivityDuration.value, inactivityDuration.unit)
    } catch (_: NoBeatsReceivedException) {
        false
    }
}