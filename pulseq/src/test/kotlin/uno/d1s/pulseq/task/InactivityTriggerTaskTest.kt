package uno.d1s.pulseq.task

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.event.impl.inactivity.InactivityDurationPointExceededEvent
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.testUtils.testTimeSpan
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener

@SpringBootTest
@ContextConfiguration(
    classes = [InactivityTriggerTask::class, ApplicationEventTestListener::class]
)
internal class InactivityTriggerTaskTest {

    @Autowired
    private lateinit var inactivityTriggerTask: InactivityTriggerTask

    @Autowired
    private lateinit var applicationEventTestListener: ApplicationEventTestListener

    @MockkBean
    private lateinit var beatService: BeatService

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            activityService.isInactivityRelevanceLevelNotCommon()
        } returns true

        every {
            activityService.getCurrentInactivityRelevanceLevel()
        } returns InactivityRelevanceLevel.LONG

        every {
            activityService.getCurrentTimeSpan()
        } returns testTimeSpan
    }

    @Test
    @Order(0)
    fun `should not trigger an event when inactivity level is just assigned`() {
        verifyPipeline(false)
    }

    @Test
    @Order(1)
    fun `should trigger an event`() {
        makeWarningRelevanceLevel()
        verifyPipeline(true)
    }

    @Test
    @Order(2)
    fun `should not trigger an event if it was already triggered`() {
        makeWarningRelevanceLevel()
        verifyPipeline(false)
    }

    private fun verifyPipeline(shouldSend: Boolean) {
        assertDoesNotThrow {
            inactivityTriggerTask.inactivityTrigger()
        }

        verify {
            activityService.isInactivityRelevanceLevelNotCommon()
        }

        verify {
            activityService.getCurrentInactivityRelevanceLevel()
        }

        if (shouldSend) {
            Assertions.assertTrue(applicationEventTestListener.isLastEventWas<InactivityDurationPointExceededEvent>())

            verify {
                activityService.getCurrentTimeSpan()
            }
        } else {
            Assertions.assertFalse(applicationEventTestListener.isLastEventWas<InactivityDurationPointExceededEvent>())
        }
    }

    private fun makeWarningRelevanceLevel() {
        every {
            activityService.getCurrentInactivityRelevanceLevel()
        } returns InactivityRelevanceLevel.WARNING
    }
}