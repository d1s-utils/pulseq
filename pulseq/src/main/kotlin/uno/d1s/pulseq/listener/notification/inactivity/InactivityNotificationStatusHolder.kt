package uno.d1s.pulseq.listener.notification.inactivity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.service.ActivityService

@Component
class InactivityNotificationStatusHolder {

    @Autowired
    lateinit var activityService: ActivityService

    lateinit var inactivityRelevanceLevel: InactivityRelevanceLevel

    fun isNotificationSent(): Boolean {
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

    fun ifNotificationIsNotSent(block: () -> Unit) {
        if (!this.isNotificationSent()) block()
    }
}