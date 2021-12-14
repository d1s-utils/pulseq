package uno.d1s.pulseq.listener

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
import uno.d1s.pulseq.configuration.property.NotificationsConfigurationProperties
import uno.d1s.pulseq.notification.NotificationSender
import uno.d1s.pulseq.testUtils.testEvent

@SpringBootTest
@ContextConfiguration(classes = [NotifiableEventListener::class])
@TestPropertySource(properties = ["pulseq.notifications.enabled=true"])
internal class NotifiableEventListenerTest {

    @Autowired
    private lateinit var notifiableEventListener: NotifiableEventListener

    @MockkBean(relaxed = true) // nothing to configure,
    // only one method with void return type
    private lateinit var notificationSender: NotificationSender

    @MockkBean
    private lateinit var notificationsConfigurationProperties: NotificationsConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            notificationsConfigurationProperties.excludeEvents
        } returns ""
    }

    @Test
    fun `should send notification on event`() {
        assertDoesNotThrow {
            notifiableEventListener.interceptNotifiableEvent(testEvent)
        }

        verify {
            notificationSender.sendNotification(testEvent)
        }
    }

    @Test
    fun `should not sent notification on event`() {
        every {
            notificationsConfigurationProperties.excludeEvents
        } returns testEvent.identify

        assertDoesNotThrow {
            notifiableEventListener.interceptNotifiableEvent(testEvent)
        }

        verify(exactly = 0) {
            notificationSender.sendNotification(testEvent)
        }
    }
}