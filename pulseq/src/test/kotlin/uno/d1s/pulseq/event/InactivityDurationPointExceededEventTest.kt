package uno.d1s.pulseq.event

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testTimeSpan
import uno.d1s.pulseq.util.pretty
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [InactivityDurationPointExceededEventTest::class])
internal final class InactivityDurationPointExceededEventTest {

    private val inactivityDurationPointExceededEvent = InactivityDurationPointExceededEvent(
        this, testTimeSpan
    )

    @Test
    fun `should return valid notification message`() {
        var message: String by Delegates.notNull()

        assertDoesNotThrow {
            message = inactivityDurationPointExceededEvent.notificationMessage
        }

        Assertions.assertEquals(
            "**${inactivityDurationPointExceededEvent.currentTimeSpan.inactivityLevel.nameString} inactivity point was just exceeded.** " + "Last beat was registered `${inactivityDurationPointExceededEvent.currentTimeSpan.duration.pretty()}` ago " + "from device `${testBeat.device.name}` with id `${testBeat.id}`.",
            message
        )
    }
}