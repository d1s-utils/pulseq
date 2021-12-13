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
import uno.d1s.pulseq.metric.impl.LastTimeSpansMetric
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.testUtils.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [LastTimeSpansMetric::class])
internal class LastTimeSpansMetricTest {

    @Autowired
    private lateinit var lastTimeSpansMetric: LastTimeSpansMetric

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            activityService.getAllTimeSpans()
        } returns listOf()
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            lastTimeSpansMetric.identify
        }

        lastTimeSpansMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastTimeSpansMetric.description
        }

        verify {
            activityService.getAllTimeSpans()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastTimeSpansMetric.shortDescription
        }
    }
}