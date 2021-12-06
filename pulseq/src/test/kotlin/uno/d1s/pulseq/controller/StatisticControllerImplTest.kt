package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.controller.advice.ExceptionHandlerControllerAdvice
import uno.d1s.pulseq.controller.impl.StatisticControllerImpl
import uno.d1s.pulseq.core.constant.mapping.StatisticMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.exception.impl.StatisticNotFoundException
import uno.d1s.pulseq.service.StatisticService
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testStatistic
import uno.d1s.pulseq.testUtils.testStatistics
import uno.d1s.pulseq.util.HttpServletResponseUtil
import uno.d1s.pulseq.util.expectJsonContentType

@WebMvcTest(useDefaultFilters = false, controllers = [StatisticControllerImpl::class])
@ContextConfiguration(classes = [StatisticControllerImpl::class, ExceptionHandlerControllerAdvice::class, HttpServletResponseUtil::class])
internal class StatisticControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var statisticService: StatisticService

    @BeforeEach
    fun setup() {
        every {
            statisticService.getAllStatistics()
        } returns testStatistics

        every {
            statisticService.getStatisticByIdentify(VALID_STUB)
        } returns testStatistic

        every {
            statisticService.getStatisticByIdentify(INVALID_STUB)
        } throws StatisticNotFoundException()
    }

    @Test
    fun `should return 200 and valid list on getting all statistics`() {
        mockMvc.get(StatisticMappingConstants.BASE).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testStatistics))
            }

            expectJsonContentType()
        }

        verify {
            statisticService.getAllStatistics()
        }
    }

    @Test
    fun `should return 200 and valid statistic on getting statistic by identify`() {
        getStatisticByIdentifyAndExpect(VALID_STUB) {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testStatistic))
            }

            expectJsonContentType()
        }

        verify {
            statisticService.getStatisticByIdentify(VALID_STUB)
        }
    }

    @Test
    fun `should return 400 on getting statistic by invalid identify`() {
        getStatisticByIdentifyAndExpect(INVALID_STUB) {
            status {
                isNotFound()
            }
        }

        verify {
            statisticService.getStatisticByIdentify(INVALID_STUB)
        }
    }

    private fun getStatisticByIdentifyAndExpect(identify: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(StatisticMappingConstants.GET_STATISTIC_BY_IDENTIFY.replacePathPlaceholder("identify", identify))
            .andExpect(block)
    }
}