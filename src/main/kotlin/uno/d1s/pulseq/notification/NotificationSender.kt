package uno.d1s.pulseq.notification

import uno.d1s.pulseq.event.NotifiableEvent

@Deprecated("Notifications are deprecated.")
interface NotificationSender {

    fun sendNotification(event: NotifiableEvent)
}