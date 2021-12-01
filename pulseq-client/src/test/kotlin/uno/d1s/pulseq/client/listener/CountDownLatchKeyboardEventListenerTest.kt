package uno.d1s.pulseq.client.listener

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningConfigurationProperties
import uno.d1s.pulseq.client.service.BeatSenderService
import uno.d1s.pulseq.client.testUtils.testKeyboardActivityDetectedEvent

@SpringBootTest
@ContextConfiguration(classes = [CountDownLatchKeyboardEventListener::class])
@TestPropertySource(properties = ["pulseq.client.keyboard-listening-mode.count-down-latch.enabled=true"])
internal class CountDownLatchKeyboardEventListenerTest {

    @Autowired
    private lateinit var countDownLatchKeyboardEventListener: CountDownLatchKeyboardEventListener

    @MockkBean(relaxUnitFun = true)
    private lateinit var beatSenderService: BeatSenderService

    @MockkBean
    private lateinit var keyboardListeningConfigurationProperties: KeyboardListeningConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            keyboardListeningConfigurationProperties.countDownLatch.countTrigger
        } returns 3
    }

    @Test
    fun `should send the beat`() {
        (0 until 3).forEach { _ ->
            assertDoesNotThrow {
                countDownLatchKeyboardEventListener.onActivity(testKeyboardActivityDetectedEvent)
            }
        }

        verify {
            beatSenderService.sendBeat()
        }
    }

    @Test
    fun `should not send the beat`() {
        countDownLatchKeyboardEventListener.onActivity(testKeyboardActivityDetectedEvent)

        verify(exactly = 0) {
            beatSenderService.sendBeat()
        }
    }
}