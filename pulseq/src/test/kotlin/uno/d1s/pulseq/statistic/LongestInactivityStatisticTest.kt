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
import uno.d1s.pulseq.statistic.impl.LongestInactivityStatistic
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.util.testTimeSpan

@SpringBootTest
@ContextConfiguration(classes = [LongestInactivityStatistic::class])
class LongestInactivityStatisticTest {

    @Autowired
    private lateinit var longestInactivityStatistic: LongestInactivityStatistic

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            activityService.getLongestInactivity()
        } returns testTimeSpan
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            longestInactivityStatistic.identify
        }

        longestInactivityStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            longestInactivityStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            longestInactivityStatistic.description
        }

        verify {
            activityService.getLongestInactivity()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            longestInactivityStatistic.shortDescription
        }
    }
}