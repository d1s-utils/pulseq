package uno.d1s.pulseq.notification

import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.configuration.property.ColorsConfigurationProperties
import uno.d1s.pulseq.event.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper
import uno.d1s.pulseq.util.pretty

@Component
@ConditionalOnProperty("pulseq.notifications.discord.enabled")
class DiscordInactivityNotificationSender : NotificationSender<InactivityDurationPointExceededEvent> {

    @Autowired
    private lateinit var webhookCluster: WebhookCluster

    @Autowired
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @Autowired
    private lateinit var colorsConfigurationProperties: ColorsConfigurationProperties

    @EventListener
    override fun sendNotification(event: InactivityDurationPointExceededEvent) {
        webhookCluster.broadcast(
            embedBuilderHelper.embedDefault {
                setColor(if (event.inactivityRelevanceLevel == InactivityRelevanceLevel.WARNING) colorsConfigurationProperties.warning else colorsConfigurationProperties.common)
                setDescription(
                    "**${event.inactivityRelevanceLevel.nameString} inactivity point was just exceeded.** Last beat was registered `${event.currentInactivity.pretty()}` ago " + "from device `${event.lastBeat.device.name}` with id `${event.lastBeat.id}`."
                )
            }
        )
    }
}