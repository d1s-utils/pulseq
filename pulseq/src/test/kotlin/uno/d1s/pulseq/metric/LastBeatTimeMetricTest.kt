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
import uno.d1s.pulseq.metric.impl.LastBeatTimeMetric
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testBeat

@SpringBootTest
@ContextConfiguration(classes = [LastBeatTimeMetric::class])
internal class LastBeatTimeMetricTest {

    @Autowired
    private lateinit var lastBeatTimeMetric: LastBeatTimeMetric

    @MockkBean
    private lateinit var beatService: BeatService

    @BeforeEach
    fun setup() {
        every {
            beatService.findLastBeat()
        } returns testBeat
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            lastBeatTimeMetric.identify
        }

        lastBeatTimeMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            lastBeatTimeMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastBeatTimeMetric.description
        }

        verify {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastBeatTimeMetric.shortDescription
        }

        verify {
            beatService.findLastBeat()
        }
    }
}