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
import uno.d1s.pulseq.metric.impl.TotalBeatsByDevicesMetric
import uno.d1s.pulseq.testUtils.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [TotalBeatsByDevicesMetric::class])
internal class TotalBeatsByDevicesMetricTest {

    @Autowired
    private lateinit var totalBeatsByDevicesMetric: TotalBeatsByDevicesMetric

    @MockkBean
    private lateinit var beatService: BeatService

    @BeforeEach
    fun setup() {
        every {
            beatService.totalBeatsByDevices()
        } returns mapOf()
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            totalBeatsByDevicesMetric.identify
        }

        totalBeatsByDevicesMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            totalBeatsByDevicesMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            totalBeatsByDevicesMetric.description
        }

        verify {
            beatService.totalBeatsByDevices()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            totalBeatsByDevicesMetric.shortDescription
        }

        verify {
            beatService.totalBeatsByDevices()
        }
    }
}