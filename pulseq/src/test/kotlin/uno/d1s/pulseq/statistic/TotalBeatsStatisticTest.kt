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
import uno.d1s.pulseq.statistic.impl.TotalBeatsStatistic
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.util.testBeat

@SpringBootTest
@ContextConfiguration(classes = [TotalBeatsStatistic::class])
class TotalBeatsStatisticTest {

    @Autowired
    private lateinit var totalBeatsStatistic: TotalBeatsStatistic

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
            totalBeatsStatistic.identify
        }

        totalBeatsStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            totalBeatsStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            totalBeatsStatistic.description
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
            totalBeatsStatistic.shortDescription
        }

        verify {
            beatService.totalBeats()
        }
    }
}