package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.configuration.property.model.notifications.DiscordWebhooksConfigurationModel

@ConfigurationProperties("pulseq.notifications")
class NotificationsConfigurationProperties(
    var enabled: Boolean = true,
    var discord: DiscordWebhooksConfigurationModel
    = DiscordWebhooksConfigurationModel(true, listOf()),
)