package uno.d1s.pulseq.notification

import org.springframework.context.ApplicationEvent

interface NotificationSender<T : ApplicationEvent> {

    fun sendNotification(event: T)
}