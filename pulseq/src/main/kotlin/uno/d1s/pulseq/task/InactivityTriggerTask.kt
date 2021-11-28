package uno.d1s.pulseq.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import java.util.concurrent.TimeUnit

@Component
class InactivityTriggerTask {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    private lateinit var inactivityRelevanceLevel: InactivityRelevanceLevel

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    fun inactivityTrigger() {
        try {
            if (activityService.isInactivityRelevanceLevelNotCommon()) {
                if (!this.isEventSent()) {
                    applicationEventPublisher.publishEvent(
                        InactivityDurationPointExceededEvent(
                            this,
                            activityService.getCurrentInactivityDuration(),
                            activityService.getCurrentInactivityRelevanceLevel(),
                            beatService.findLastBeat()
                        )
                    )
                }
            }
            // It's fine, the app running in a first time, so there are no
            // beats in the database.
        } catch (_: NoBeatsReceivedException) {
        }
    }

    private fun isEventSent(): Boolean {
        if (!this::inactivityRelevanceLevel.isInitialized) {
            inactivityRelevanceLevel = activityService.getCurrentInactivityRelevanceLevel()
            return false
        }

        return if (inactivityRelevanceLevel == activityService.getCurrentInactivityRelevanceLevel()) {
            true
        } else {
            inactivityRelevanceLevel = activityService.getCurrentInactivityRelevanceLevel()
            false
        }
    }

}