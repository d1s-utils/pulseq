package uno.d1s.pulseq.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.impl.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.service.ActivityService
import java.util.concurrent.TimeUnit

@Component
class InactivityTriggerTask {

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
                            this, activityService.getCurrentTimeSpan()
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
        val relevanceLevel = activityService.getCurrentInactivityRelevanceLevel()

        return if (!this::inactivityRelevanceLevel.isInitialized) {
            inactivityRelevanceLevel = relevanceLevel
            true // it's fine, we won't send an event when application is just started
        } else if (inactivityRelevanceLevel != relevanceLevel) {
            inactivityRelevanceLevel = relevanceLevel
            false
        } else {
            true
        }
    }
}