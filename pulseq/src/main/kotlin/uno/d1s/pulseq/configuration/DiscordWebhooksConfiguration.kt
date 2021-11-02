package uno.d1s.pulseq.configuration

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.WebhookCluster
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uno.d1s.pulseq.configuration.property.NotificationsConfigurationProperties

@Configuration
@ConditionalOnProperty("pulseq.notifications.discord.enabled")
class DiscordWebhooksConfiguration {

    @Autowired
    private lateinit var notificationsConfigurationProperties: NotificationsConfigurationProperties

    private val logger = LogManager.getLogger()

    @Bean
    fun webhookCluster(): WebhookCluster = WebhookCluster().apply {
        addWebhooks(
            notificationsConfigurationProperties.discord.webhooks.map {
                WebhookClient.withUrl(it)
            }.also {
                if (it.isEmpty()) {
                    logger.warn("Discord webhooks notifications is enabled but the empty webhooks urls list was provided.")
                }
            }
        )
    }
}