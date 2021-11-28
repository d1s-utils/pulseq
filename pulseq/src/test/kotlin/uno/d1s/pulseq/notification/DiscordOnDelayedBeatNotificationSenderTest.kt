package uno.d1s.pulseq.notification

import club.minnced.discord.webhook.WebhookCluster
import club.minnced.discord.webhook.send.WebhookEmbed
import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import uno.d1s.pulseq.configuration.property.ColorsConfigurationProperties
import uno.d1s.pulseq.event.DelayedBeatReceivedEvent
import uno.d1s.pulseq.testBeat
import uno.d1s.pulseq.util.WebhookEmbedBuilderHelper

@SpringBootTest
@TestPropertySource(properties = ["pulseq.notifications.discord.enabled=true"])
class DiscordOnDelayedBeatNotificationSenderTest {

    @Autowired
    private lateinit var discordOnDelayedBeatNotificationSender: DiscordOnDelayedBeatNotificationSender

    @MockkBean(relaxed = true)
    private lateinit var webhookCluster: WebhookCluster

    @MockkBean(relaxed = true)
    private lateinit var embedBuilderHelper: WebhookEmbedBuilderHelper

    @MockkBean(relaxed = true)
    private lateinit var colorsConfigurationProperties: ColorsConfigurationProperties

    @Test
    fun `should send notification on delayed beat`() {
        discordOnDelayedBeatNotificationSender.sendNotification(DelayedBeatReceivedEvent(this, testBeat))

        verify {
            webhookCluster.broadcast(any<WebhookEmbed>())
        }

        verify {
            embedBuilderHelper.embedDefault(any())
        }
    }
}