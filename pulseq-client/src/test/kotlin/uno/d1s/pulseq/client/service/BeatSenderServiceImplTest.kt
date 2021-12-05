package uno.d1s.pulseq.client.service

import com.ninjasquad.springmockk.MockkBean
import io.ktor.client.*
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.client.configuration.properties.ClientConfigurationProperties
import uno.d1s.pulseq.client.service.impl.BeatSenderServiceImpl

@SpringBootTest
@ContextConfiguration(classes = [BeatSenderServiceImpl::class])
internal class BeatSenderServiceImplTest {

    @Autowired
    private lateinit var beatSenderServiceImpl: BeatSenderServiceImpl

    @MockkBean(relaxed = true)
    private lateinit var clientConfigurationProperties: ClientConfigurationProperties

    @MockkBean(relaxed = true)
    private lateinit var httpClient: HttpClient

    @Test
    fun `should send the beat`() {
        assertDoesNotThrow {
            beatSenderServiceImpl.sendBeat()
        }

        verify {
            clientConfigurationProperties.deviceName
        }

        verify {
            clientConfigurationProperties.serverUrl
        }

        verify {
            clientConfigurationProperties.deviceName
        }

        verify {
            clientConfigurationProperties.serverSecret
        }

        // could not verify post request here
        // see https://github.com/mockk/mockk/issues/718
    }
}