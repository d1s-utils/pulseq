package uno.d1s.pulseq.configuration.property.model.notifications

@Deprecated("Notifications are deprecated.")
class DiscordWebhooksConfigurationModel(
    override var enabled: Boolean,
    var webhooks: List<String>
) : NotificationServiceConfigurationModel