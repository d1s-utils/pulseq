package uno.d1s.pulseq.statistic

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
import uno.d1s.pulseq.statistic.impl.LastTimeSpansStatistic
import uno.d1s.pulseq.util.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [LastTimeSpansStatistic::class])
internal class LastTimeSpansStatisticTest {

    @Autowired
    private lateinit var lastTimeSpansStatistic: LastTimeSpansStatistic

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
            lastTimeSpansStatistic.identify
        }

        lastTimeSpansStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastTimeSpansStatistic.description
        }

        verify {
            activityService.getAllTimeSpans()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastTimeSpansStatistic.shortDescription
        }
    }
}