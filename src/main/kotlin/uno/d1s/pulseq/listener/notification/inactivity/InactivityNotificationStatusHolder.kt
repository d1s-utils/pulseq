package uno.d1s.pulseq.listener.notification.inactivity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.service.InactivityStatusService

@Component
class InactivityNotificationStatusHolder {

    @Autowired
    lateinit var inactivityService: InactivityStatusService

    lateinit var inactivityRelevanceLevel: InactivityRelevanceLevel

    fun isNotificationSent(): Boolean {
        if (!this::inactivityRelevanceLevel.isInitialized) {
            inactivityRelevanceLevel = inactivityService.getCurrentInactivityRelevanceLevel()
            return false
        }

        return if (inactivityRelevanceLevel == inactivityService.getCurrentInactivityRelevanceLevel()) {
            true
        } else {
            inactivityRelevanceLevel = inactivityService.getCurrentInactivityRelevanceLevel()
            false
        }
    }

    fun ifNotificationIsNotSent(block: () -> Unit) {
        if (!this.isNotificationSent()) block()
    }
}