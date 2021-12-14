package uno.d1s.pulseq.event.device

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.device.DeviceDeletedEvent
import uno.d1s.pulseq.testUtils.testDevice
import uno.d1s.pulseq.util.pluralGrammar

@SpringBootTest
@ContextConfiguration(classes = [DeviceDeletedEventTest::class])
internal final class DeviceDeletedEventTest {

    private val deviceDeletedEvent = DeviceDeletedEvent(
        this, testDevice
    )

    @Test
    fun `should return valid identify`() {
        Assertions.assertEquals("device-deleted-event", deviceDeletedEvent.identify)
    }

    @Test
    fun `should return valid notification message`() {
        Assertions.assertEquals(
            "A device with id `${testDevice.id!!}` and `${testDevice.beats!!.size}` registered beat${
                pluralGrammar(
                    testDevice.beats!!.size
                )
            } were just deleted.", deviceDeletedEvent.notificationMessage
        )
    }
}