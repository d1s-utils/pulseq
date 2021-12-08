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
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.metric.impl.TotalBeatsMetric
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testBeat

@SpringBootTest
@ContextConfiguration(classes = [TotalBeatsMetric::class])
internal class TotalBeatsMetricTest {

    @Autowired
    private lateinit var totalBeatsMetric: TotalBeatsMetric

    @MockkBean
    private lateinit var beatService: BeatService

    @BeforeEach
    fun setup() {
        every {
            beatService.findFirstBeat()
        } returns testBeat

        every {
            beatService.totalBeats()
        } returns 1
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            totalBeatsMetric.identify
        }

        totalBeatsMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            totalBeatsMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            totalBeatsMetric.description
        }

        verify {
            beatService.totalBeats()
        }

        verify {
            beatService.findFirstBeat()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            totalBeatsMetric.shortDescription
        }

        verify {
            beatService.totalBeats()
        }
    }
}