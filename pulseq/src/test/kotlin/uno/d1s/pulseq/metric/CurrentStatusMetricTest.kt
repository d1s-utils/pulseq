package uno.d1s.pulseq.metric

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.impl.CurrentStatusMetric
import uno.d1s.pulseq.testUtils.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testTimeSpan

@SpringBootTest
@ContextConfiguration(classes = [CurrentStatusMetric::class])
internal class CurrentStatusMetricTest {

    @Autowired
    private lateinit var currentStatusMetric: CurrentStatusMetric

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            activityService.getCurrentTimeSpan()
        } returns testTimeSpan
    }

    @Test
    fun `should return identify`() {
        assertDoesNotThrow {
            currentStatusMetric.identify
        }

        currentStatusMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            currentStatusMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            currentStatusMetric.description
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            currentStatusMetric.shortDescription
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }
}