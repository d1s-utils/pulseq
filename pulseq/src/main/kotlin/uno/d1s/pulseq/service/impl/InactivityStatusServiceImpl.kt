package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.util.pretty
import java.time.Duration
import java.time.Instant

@Service("inactivityStatusService")
class InactivityStatusServiceImpl : ActivityService {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

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

    override fun isInactivityRelevanceLevelNotCommon(): Boolean =
        this.getCurrentInactivityRelevanceLevel() != InactivityRelevanceLevel.COMMON

    private fun isLongInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(activityConfigurationProperties.common)

    private fun isWarningInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(activityConfigurationProperties.warning)

    private fun isDurationPointExceeded(duration: Duration) = try {
        this.getCurrentInactivity() > duration
    } catch (_: NoBeatsReceivedException) {
        false
    }
}