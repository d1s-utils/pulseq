package uno.d1s.pulseq.notification

import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.configuration.property.ColorsConfigurationProperties
import uno.d1s.pulseq.event.DelayedBeatReceivedEvent
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper
import uno.d1s.pulseq.util.pretty

@Component
@ConditionalOnProperty("pulseq.notifications.discord.enabled")
class DiscordOnDelayedBeatNotificationSender : NotificationSender<DelayedBeatReceivedEvent> {

    @Autowired
    private lateinit var webhookCluster: WebhookCluster

    @Autowired
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @Autowired
    private lateinit var colorsConfigurationProperties: ColorsConfigurationProperties

    @EventListener
    override fun sendNotification(event: DelayedBeatReceivedEvent) {
        webhookCluster.broadcast(
            embedBuilderHelper.embedDefault {
                setColor(colorsConfigurationProperties.common)
                setDescription(
                    event.beat.inactivityBeforeBeat.let {
                        "A beat with id `${event.beat.id}` was just received" + if (it == null) "!" else " after `${it.pretty()}` of inactivity!"
                    }
                )
            }
        )
    }
}