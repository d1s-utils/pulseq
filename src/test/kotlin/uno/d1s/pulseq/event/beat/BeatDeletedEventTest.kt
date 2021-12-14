package uno.d1s.pulseq.event.beat

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.beat.BeatDeletedEvent
import uno.d1s.pulseq.testUtils.testBeat

@SpringBootTest
@ContextConfiguration(classes = [BeatDeletedEventTest::class])
internal final class BeatDeletedEventTest {

    private val beatDeletedEvent = BeatDeletedEvent(
        this, testBeat
    )

    @Test
    fun `should return valid identify`() {
        Assertions.assertEquals("beat-deleted-event", beatDeletedEvent.identify)
    }

    @Test
    fun `should return valid notification message`() {
        Assertions.assertEquals(
            "A beat with id `${testBeat.id!!}` was just deleted.",
            beatDeletedEvent.notificationMessage
        )
    }
}