package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.configuration.property.StatisticsConfigurationProperties
import uno.d1s.pulseq.exception.impl.StatisticNotFoundException
import uno.d1s.pulseq.service.impl.StatisticServiceImpl
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import kotlin.properties.Delegates


@SpringBootTest
@ContextConfiguration(classes = [StatisticServiceImpl::class])
internal class StatisticServiceImplTest {

    @SpykBean
    private lateinit var statisticService: StatisticServiceImpl

    @MockkBean(relaxed = true)
    private lateinit var statistic: Statistic

    @MockkBean(relaxed = true)
    private lateinit var statisticsConfigurationProperties: StatisticsConfigurationProperties

    private val allStatistics by lazy {
        listOf(statistic)
    }

    @BeforeEach
    fun setup() {
        every {
            statistic.identify
        } returns VALID_STUB
    }

    @Test
    fun `should find all statistics`() {
        var all: List<Statistic> by Delegates.notNull()

        assertDoesNotThrow {
            all = statisticService.getAllStatistics()
        }

        verify {
            statistic.identify
        }

        Assertions.assertEquals(allStatistics, all)
    }

    @Test
    fun `should find statistic by identify`() {
        var statisticByIdentify: Statistic by Delegates.notNull()

        assertDoesNotThrow {
            statisticByIdentify = statisticService.getStatisticByIdentify(VALID_STUB)
        }

        verify {
            statistic.identify
        }

        Assertions.assertEquals(statistic, statisticByIdentify)
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
        var formattedStatistics: String by Delegates.notNull()

        assertDoesNotThrow {
            formattedStatistics = statisticService.getStatisticsFormatted()
        }

        verify {
            statisticService.getAllStatistics()
        }

        Assertions.assertEquals("${statistic.title}: ${statistic.description}", formattedStatistics)
    }
}