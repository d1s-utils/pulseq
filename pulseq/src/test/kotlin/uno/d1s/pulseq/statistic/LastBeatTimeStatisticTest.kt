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
import uno.d1s.pulseq.statistic.impl.LastBeatTimeStatistic
import uno.d1s.pulseq.util.assertNoWhitespace
import uno.d1s.pulseq.testUtils.testBeat

@SpringBootTest
@ContextConfiguration(classes = [LastBeatTimeStatistic::class])
internal class LastBeatTimeStatisticTest {

    @Autowired
    private lateinit var lastBeatTimeStatistic: LastBeatTimeStatistic

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
            lastBeatTimeStatistic.identify
        }

        lastBeatTimeStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            lastBeatTimeStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            lastBeatTimeStatistic.description
        }

        verify {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            lastBeatTimeStatistic.shortDescription
        }

        verify {
            beatService.findLastBeat()
        }
    }
}