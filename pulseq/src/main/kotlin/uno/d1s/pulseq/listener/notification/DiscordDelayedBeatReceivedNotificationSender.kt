package uno.d1s.pulseq.listener.notification

import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.event.DelayedBeatReceivedEvent
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper
import uno.d1s.pulseq.util.pretty

@Component
@ConditionalOnProperty("pulseq.notifications.discord.enabled")
class DiscordDelayedBeatReceivedNotificationSender {

    @Autowired
    private lateinit var webhookCluster: WebhookCluster

    @Autowired
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @EventListener
    fun sendNotification(event: DelayedBeatReceivedEvent) {
        webhookCluster.broadcast(
            embedBuilderHelper.embedDefault {
                // see https://colorpicker.me/#b4ff77
                setColor(0xb4ff77)
                setDescription(
                    event.beat.inactivityBeforeBeat.let {
                        "A beat with id `${event.beat.id}` was just received" + if (it == null) "!" else " after `${it.pretty()}` of inactivity!"
                    }
                )
            }
        )
    }
}