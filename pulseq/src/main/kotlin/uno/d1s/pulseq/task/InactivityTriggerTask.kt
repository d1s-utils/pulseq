package uno.d1s.pulseq.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.ActivityService
import java.util.concurrent.TimeUnit

@Component
class InactivityTriggerTask {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    fun inactivityTrigger() {
        try {
            if (activityService.isInactivityRelevanceLevelNotCommon()) {
                applicationEventPublisher.publishEvent(
                    InactivityDurationPointExceededEvent(
                        this,
                        activityService.getCurrentInactivity(),
                        activityService.getCurrentInactivityRelevanceLevel(),
                        beatService.findLastBeat()
                    )
                )
            }
            // It's fine, the app running in a first time, so there are no
            // beats in the database.
        } catch (_: NoBeatsReceivedException) {
        }
    }
}