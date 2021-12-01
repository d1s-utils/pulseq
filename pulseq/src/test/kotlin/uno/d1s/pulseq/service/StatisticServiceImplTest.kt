package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.configuration.property.StatisticsConfigurationProperties
import uno.d1s.pulseq.exception.StatisticNotFoundException
import uno.d1s.pulseq.service.impl.StatisticServiceImpl
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB


@SpringBootTest
@ContextConfiguration(classes = [StatisticServiceImpl::class])
internal class StatisticServiceImplTest {

    @Autowired
    private lateinit var statisticService: StatisticServiceImpl

    @MockkBean(relaxed = true)
    private lateinit var statistic: Statistic

    @MockkBean(relaxed = true)
    private lateinit var statisticsConfigurationProperties: StatisticsConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            statistic.identify
        } returns VALID_STUB
    }

    @Test
    fun `should find all statistics`() {
        assertDoesNotThrow {
            statisticService.getAllStatistics()
        }

        verify {
            statistic.identify
        }
    }

    @Test
    fun `should find statistic by identify`() {
        assertDoesNotThrow {
            statisticService.getStatisticByIdentify(VALID_STUB)
        }

        verify {
            statistic.identify
        }
    }

    @Test
    fun `should throw an exception on getting the statistic by invalid id`() {
        Assertions.assertThrows(StatisticNotFoundException::class.java) {
            statisticService.getStatisticByIdentify(INVALID_STUB)
        }

        verify {
            statistic.identify
        }
    }

    @Test
    fun `should return the formatted statistics`() {
        assertDoesNotThrow {
            statisticService.getStatisticsFormatted()
        }
    }
}