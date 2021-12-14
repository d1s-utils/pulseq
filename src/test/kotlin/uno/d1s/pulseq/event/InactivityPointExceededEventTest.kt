package uno.d1s.pulseq.event

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.InactivityPointExceededEvent
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testTimeSpan
import uno.d1s.pulseq.util.pretty
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [InactivityPointExceededEventTest::class])
internal final class InactivityPointExceededEventTest {

    private val inactivityPointExceededEvent = InactivityPointExceededEvent(
        this, testTimeSpan
    )

    @Test
    fun `should return valid identify`() {
        Assertions.assertEquals("inactivity-point-exceeded-event", inactivityPointExceededEvent.identify)
    }

    @Test
    fun `should return valid notification message`() {
        var message: String by Delegates.notNull()

        assertDoesNotThrow {
            message = inactivityPointExceededEvent.notificationMessage
        }

        Assertions.assertEquals(
            "${inactivityPointExceededEvent.currentTimeSpan.inactivityLevel.nameString} inactivity point was just exceeded. Last beat was registered `${inactivityPointExceededEvent.currentTimeSpan.duration.pretty()}` ago from device `${testBeat.device.name}` with id `${testBeat.id}`.",
            message
        )
    }
}