package uno.d1s.pulseq.listener.notification.inactivity

import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper
import uno.d1s.pulseq.util.pretty

@Component
@ConditionalOnProperty(prefix = "pulseq.notifications.discord", name = ["enabled"])
class DiscordInactivityNotificationSender {

    @Autowired
    private lateinit var webhookCluster: WebhookCluster

    @Autowired
    private lateinit var inactivityNotificationStatusHolder: InactivityNotificationStatusHolder

    @Autowired
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @EventListener
    fun sendNotification(event: InactivityDurationPointExceededEvent) {
        inactivityNotificationStatusHolder.ifNotificationIsNotSent {
            webhookCluster.broadcast(
                embedBuilderHelper.embedDefault {
                    // see https://colorpicker.me/#ff4935, https://colorpicker.me/#ffce360
                    setColor(if (event.inactivityRelevanceLevel == InactivityRelevanceLevel.WARNING) 0xff4935 else 0xffce360)
                    setDescription(
                        "**${event.inactivityRelevanceLevel.nameString} inactivity point was just exceeded.** Last beat was registered `${event.currentInactivity.pretty()}` ago " +
                                "from device `${event.lastBeat.device.name}` with id `${event.lastBeat.id}`."
                    )
                }
            )
        }
    }
}