package uno.d1s.pulseq.listener

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.configuration.property.NotificationsConfigurationProperties
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.notification.NotificationSender
import uno.d1s.pulseq.util.fromCommaSeparatedString

@Component
@ConditionalOnProperty("pulseq.notifications.enabled")
class NotifiableEventListener {

    @Autowired(required = false)
    private lateinit var notificationSenders: List<NotificationSender>

    @Autowired
    private lateinit var notificationsConfigurationProperties: NotificationsConfigurationProperties

    @EventListener
    fun interceptNotifiableEvent(event: AbstractNotifiableEvent) {
        if (!notificationsConfigurationProperties.excludeEvents.fromCommaSeparatedString().contains(event.identify))
            notificationSenders.forEach {
                it.sendNotification(event)
            }
    }
}