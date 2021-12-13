package uno.d1s.pulseq.notification

import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import uno.d1s.pulseq.configuration.property.ColorsConfigurationProperties
import uno.d1s.pulseq.event.NotifiableEvent
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper

@Component
@ConditionalOnProperty("pulseq.notifications.discord.enabled")
class DiscordNotificationSender : NotificationSender {

    @Autowired
    private lateinit var webhookCluster: WebhookCluster

    @Autowired
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @Autowired
    private lateinit var colorsConfigurationProperties: ColorsConfigurationProperties

    override fun sendNotification(event: NotifiableEvent) {
        webhookCluster.broadcast(
            embedBuilderHelper.embedDefault {
                setColor(colorsConfigurationProperties.common)
                setDescription(event.notificationMessage)
            }
        )
    }
}