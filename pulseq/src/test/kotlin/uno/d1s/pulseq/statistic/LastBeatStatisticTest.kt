package uno.d1s.pulseq.statistic

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.statistic.impl.LastBeatStatistic
import uno.d1s.pulseq.util.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [LastBeatStatistic::class])
internal class LastBeatStatisticTest {

    @Autowired
    private lateinit var lastBeatStatistic: LastBeatStatistic

    @MockkBean(relaxed = true)
    private lateinit var activityService: ActivityService

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            lastBeatStatistic.identify
        }

        lastBeatStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            lastBeatStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastBeatStatistic.description
        }

        verify {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastBeatStatistic.shortDescription
        }

        verify {
            activityService.getCurrentInactivityPretty()
        }
    }
}