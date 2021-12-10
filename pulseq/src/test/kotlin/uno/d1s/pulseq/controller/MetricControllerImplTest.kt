package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.controller.impl.MetricControllerImpl
import uno.d1s.pulseq.core.constant.mapping.MetricMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.service.MetricService
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testMetric
import uno.d1s.pulseq.testUtils.testMetrics
import uno.d1s.pulseq.testUtils.expectJsonContentType

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [MetricControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [MetricControllerImpl::class])
internal class MetricControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var metricService: MetricService

    @BeforeEach
    fun setup() {
        every {
            metricService.getAllMetrics()
        } returns testMetrics

        every {
            metricService.getMetricByIdentify(VALID_STUB)
        } returns testMetric
    }

    @Test
    fun `should return 200 and valid list on getting all metrics`() {
        mockMvc.get(MetricMappingConstants.BASE).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testMetrics))
            }

            expectJsonContentType()
        }

        verify {
            metricService.getAllMetrics()
        }
    }

    @Test
    fun `should return 200 and valid metric on getting metric by identify`() {
        mockMvc.get(MetricMappingConstants.GET_METRIC_BY_IDENTIFY.replacePathPlaceholder("identify", VALID_STUB))
            .andExpect {
                status {
                    isOk()
                }

                content {
                    json(objectMapper.writeValueAsString(testMetric))
                }

                expectJsonContentType()
            }

        verify {
            metricService.getMetricByIdentify(VALID_STUB)
        }
    }
}