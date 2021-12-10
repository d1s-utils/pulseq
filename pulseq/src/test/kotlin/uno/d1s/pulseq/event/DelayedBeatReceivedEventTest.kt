package uno.d1s.pulseq.event

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.event.impl.DelayedBeatReceivedEvent
import uno.d1s.pulseq.util.pretty
import java.time.Duration

@SpringBootTest
@ContextConfiguration(classes = [DelayedBeatReceivedEventTest::class])
internal final class DelayedBeatReceivedEventTest {

    private val mockBeat: Beat = mockk(relaxed = true)

    private var delayedBeatReceivedEvent = DelayedBeatReceivedEvent(
        this, mockBeat
    )

    @BeforeEach
    fun setup() {
        every {
            mockBeat.inactivityBeforeBeat
        } returns Duration.ZERO
    }

    @Test
    @Order(0)
    fun `should return valid notification message`() {
        Assertions.assertEquals(
            "A beat with id `${mockBeat.id}` was just received after `${mockBeat.inactivityBeforeBeat!!.pretty()}` of inactivity.",
            delayedBeatReceivedEvent.notificationMessage
        )
    }

    @Test
    fun `should return valid notification message with null inactivity before beat`() {
        every {
            mockBeat.inactivityBeforeBeat
        } returns null

        Assertions.assertEquals(
            "A beat with id `${mockBeat.id}` was just received.",
            delayedBeatReceivedEvent.notificationMessage
        )
    }
}