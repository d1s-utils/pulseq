package uno.d1s.pulseq.configuration.property.model.notifications

import org.springframework.util.StringUtils

class DiscordWebhooksConfigurationModel(
    override var enabled: Boolean,
    private var webhooks: String
) : NotificationServiceConfigurationModel {

    fun parsedWebhooks(): MutableSet<String> = StringUtils.commaDelimitedListToSet(webhooks)
}