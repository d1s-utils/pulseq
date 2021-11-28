package uno.d1s.pulseq.listener

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import uno.d1s.pulseq.notification.NotificationSender
import uno.d1s.pulseq.testEvent

@SpringBootTest
@TestPropertySource(properties = ["pulseq.notifications.enabled=true"])
class NotifiableEventListenerTest {

    @Autowired
    private lateinit var notifiableEventListener: NotifiableEventListener

    @MockkBean(relaxed = true) // nothing to configure,
    // only one method with void return type
    private lateinit var notificationSender: NotificationSender

    @Test
    fun `should send notification on event`() {
        notifiableEventListener.interceptNotifiableEvent(testEvent)

        verify {
            notificationSender.sendNotification(any())
        }
    }
}