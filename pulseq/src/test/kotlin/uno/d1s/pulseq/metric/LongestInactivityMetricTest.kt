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
import uno.d1s.pulseq.metric.impl.LongestInactivityMetric
import uno.d1s.pulseq.testUtils.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testTimeSpan

@SpringBootTest
@ContextConfiguration(classes = [LongestInactivityMetric::class])
internal class LongestInactivityMetricTest {

    @Autowired
    private lateinit var longestInactivityMetric: LongestInactivityMetric

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            activityService.getLongestTimeSpan()
        } returns testTimeSpan
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            longestInactivityMetric.identify
        }

        longestInactivityMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            longestInactivityMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            longestInactivityMetric.description
        }

        verify {
            activityService.getLongestTimeSpan()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            longestInactivityMetric.shortDescription
        }
    }
}