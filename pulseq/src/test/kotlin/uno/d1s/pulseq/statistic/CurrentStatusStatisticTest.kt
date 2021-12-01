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
import uno.d1s.pulseq.statistic.impl.CurrentStatusStatistic
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testTimeSpan

@SpringBootTest
@ContextConfiguration(classes = [CurrentStatusStatistic::class])
internal class CurrentStatusStatisticTest {

    @Autowired
    private lateinit var currentStatusStatistic: CurrentStatusStatistic

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
            currentStatusStatistic.identify
        }

        currentStatusStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            currentStatusStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            currentStatusStatistic.description
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            currentStatusStatistic.shortDescription
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }
}