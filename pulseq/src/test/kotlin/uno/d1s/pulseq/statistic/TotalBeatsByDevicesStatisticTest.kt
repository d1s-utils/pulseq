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
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.statistic.impl.TotalBeatsByDevicesStatistic
import uno.d1s.pulseq.util.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [TotalBeatsByDevicesStatistic::class])
class TotalBeatsByDevicesStatisticTest {

    @Autowired
    private lateinit var totalBeatsByDevicesStatistic: TotalBeatsByDevicesStatistic

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
            totalBeatsByDevicesStatistic.identify
        }

        totalBeatsByDevicesStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            totalBeatsByDevicesStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            totalBeatsByDevicesStatistic.description
        }

        verify {
            beatService.totalBeatsByDevices()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            totalBeatsByDevicesStatistic.shortDescription
        }

        verify {
            beatService.totalBeatsByDevices()
        }
    }
}