package uno.d1s.pulseq.event.device

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.device.DeviceUpdatedEvent
import uno.d1s.pulseq.testUtils.testDevice
import uno.d1s.pulseq.testUtils.testDeviceUpdate

@SpringBootTest
@ContextConfiguration(classes = [DeviceUpdatedEventTest::class])
internal final class DeviceUpdatedEventTest {

    private val deviceUpdatedEvent = DeviceUpdatedEvent(
        this, testDevice, testDeviceUpdate
    )

    @Test
    fun `should return valid identify`() {
        Assertions.assertEquals("device-updated-event", deviceUpdatedEvent.identify)
    }

    @Test
    fun `should return valid notification message`() {
        Assertions.assertEquals(
            "A device with id `${testDevice.id!!}` was just updated. From `${testDevice}` to ${testDeviceUpdate}.",
            deviceUpdatedEvent.notificationMessage
        )
    }
}