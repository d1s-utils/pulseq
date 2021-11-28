package uno.d1s.pulseq.notification

import uno.d1s.pulseq.event.NotifiableEvent

interface NotificationSender {

    fun sendNotification(event: NotifiableEvent)
}