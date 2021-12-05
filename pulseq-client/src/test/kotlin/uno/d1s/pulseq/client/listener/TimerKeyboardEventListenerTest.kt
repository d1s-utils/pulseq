package uno.d1s.pulseq.client.listener

import com.ninjasquad.springmockk.MockkBean
import io.mockk.called
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningConfigurationProperties
import uno.d1s.pulseq.client.service.BeatSenderService
import uno.d1s.pulseq.client.testUtils.testKeyboardActivityDetectedEvent
import java.time.Duration

@SpringBootTest
@ContextConfiguration(classes = [TimerKeyboardEventListener::class])
@TestPropertySource(properties = ["pulseq.client.keyboard-listening-mode.timer.enabled=true"])
internal class TimerKeyboardEventListenerTest {

    @Autowired
    private lateinit var timerKeyboardEventListener: TimerKeyboardEventListener

    @MockkBean(relaxUnitFun = true)
    private lateinit var beatSenderService: BeatSenderService

    @MockkBean
    private lateinit var keyboardListeningConfigurationProperties: KeyboardListeningConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            keyboardListeningConfigurationProperties.timer.delay
        } returns Duration.ofMillis(10)
    }

    @Test
    fun `should send the beat`() {
        runBlocking {
            timerKeyboardEventListener.onActivity(testKeyboardActivityDetectedEvent)

            delay(15)

            verify {
                beatSenderService.sendBeat()
            }
        }
    }

    @Test
    fun `should not send the beat`() {
        timerKeyboardEventListener.onActivity(testKeyboardActivityDetectedEvent)

        verify(exactly = 0) {
            beatSenderService.sendBeat()
        }
    }
}