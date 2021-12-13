package uno.d1s.pulseq.util

import club.minnced.discord.webhook.send.WebhookEmbed
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [WebhookEmbedBuilderHelper::class])
internal class WebhookEmbedBuilderHelperTest {

    @Autowired
    private lateinit var webhookEmbedBuilderHelper: WebhookEmbedBuilderHelper

    @MockkBean(relaxed = true)
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    @Test
    fun `should return the embed`() {
        var embed: WebhookEmbed by Delegates.notNull()

        assertDoesNotThrow {
            embed = webhookEmbedBuilderHelper.embedDefault {
                setDescription("ok")
            }
        }

        Assertions.assertEquals("ok", embed.description)
    }
}