package uno.d1s.pulseq.util

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import java.time.OffsetDateTime

@Component
class WebhookEmbedBuilderHelper {

    @Autowired
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    fun embedDefault(builder: WebhookEmbedBuilder.() -> Unit): WebhookEmbed =
        WebhookEmbedBuilder().apply {
            setTimestamp(OffsetDateTime.now())
            setFooter(WebhookEmbed.EmbedFooter("Pulseq | ${globalConfigurationProperties.version}", null))
            builder()
        }.build()
}
