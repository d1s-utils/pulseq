package uno.d1s.pulseq.metric

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.impl.LastBeatMetric
import uno.d1s.pulseq.testUtils.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [LastBeatMetric::class])
internal class LastBeatMetricTest {

    @Autowired
    private lateinit var lastBeatMetric: LastBeatMetric

    @MockkBean(relaxed = true)
    private lateinit var activityService: ActivityService

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            lastBeatMetric.identify
        }

        lastBeatMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            lastBeatMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastBeatMetric.description
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastBeatMetric.shortDescription
        }

        verify {
            activityService.getCurrentInactivityPretty()
        }
    }
}