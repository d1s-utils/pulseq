package uno.d1s.pulseq.configuration

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.WebhookCluster
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uno.d1s.pulseq.configuration.property.NotificationsConfigurationProperties

@Configuration
@ConditionalOnProperty(prefix = "pulseq.notifications.discord", name = ["enabled"])
class DiscordWebhooksClusterConfiguration {

    @Autowired
    private lateinit var notificationsConfigurationProperties: NotificationsConfigurationProperties

    @Bean
    fun webhookCluster(): WebhookCluster = WebhookCluster().apply {
        addWebhooks(
            notificationsConfigurationProperties.discord.webhooks.map {
                WebhookClient.withUrl(it)
            }
        )
    }
}