package uno.d1s.pulseq.listener

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.notification.NotificationSender

@Component
@ConditionalOnProperty("pulseq.notifications.enabled")
class NotifiableEventListener {

    @Autowired(required = false)
    private lateinit var notificationSenders: List<NotificationSender>

    @EventListener
    fun interceptNotifiableEvent(event: AbstractNotifiableEvent) {
        notificationSenders.forEach {
            it.sendNotification(event)
        }
    }
}