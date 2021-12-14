package uno.d1s.pulseq.event.beat

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.impl.beat.BeatReceivedEvent
import uno.d1s.pulseq.util.pretty
import java.time.Duration

@SpringBootTest
@ContextConfiguration(classes = [BeatReceivedEventTest::class])
internal final class BeatReceivedEventTest {

    private val mockBeat: Beat = mockk(relaxed = true)

    private val beatReceivedEvent
        get() = BeatReceivedEvent(
            this, mockBeat, true
        )

    @BeforeEach
    fun setup() {
        every {
            mockBeat.inactivityBeforeBeat
        } returns Duration.ZERO
    }

    @Test
    fun `should return valid identify`() {
        Assertions.assertEquals("beat-received-event", beatReceivedEvent.identify)
    }

    @Test
    fun `should return valid notification message`() {
        Assertions.assertEquals(
            "A delayed beat with id `${mockBeat.id}` was just received after `${mockBeat.inactivityBeforeBeat!!.pretty()}` of inactivity from device `${mockBeat.device.name}`.",
            beatReceivedEvent.notificationMessage
        )
    }

    @Test
    fun `should return valid notification message with null inactivity before beat`() {
        every {
            mockBeat.inactivityBeforeBeat
        } returns null

        Assertions.assertEquals(
            "A delayed beat with id `${mockBeat.id}` was just received from device `${mockBeat.device.name}`.",
            beatReceivedEvent.notificationMessage
        )
    }
}