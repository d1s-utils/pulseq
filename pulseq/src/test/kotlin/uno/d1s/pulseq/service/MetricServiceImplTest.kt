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
import uno.d1s.pulseq.configuration.property.MetricsConfigurationProperties
import uno.d1s.pulseq.exception.impl.MetricNotFoundException
import uno.d1s.pulseq.service.impl.MetricServiceImpl
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import kotlin.properties.Delegates


@SpringBootTest
@ContextConfiguration(classes = [MetricServiceImpl::class])
internal class MetricServiceImplTest {

    @SpykBean
    private lateinit var metricService: MetricServiceImpl

    @MockkBean(relaxed = true)
    private lateinit var metric: Metric

    @MockkBean(relaxed = true)
    private lateinit var metricsConfigurationProperties: MetricsConfigurationProperties

    private val allMetrics by lazy {
        listOf(metric)
    }

    @BeforeEach
    fun setup() {
        every {
            metric.identify
        } returns VALID_STUB
    }

    @Test
    fun `should find all metrics`() {
        var all: List<Metric> by Delegates.notNull()

        assertDoesNotThrow {
            all = metricService.getAllMetrics()
        }

        verify {
            metric.identify
        }

        Assertions.assertEquals(allMetrics, all)
    }

    @Test
    fun `should find metric by identify`() {
        var metricByIdentify: Metric by Delegates.notNull()

        assertDoesNotThrow {
            metricByIdentify = metricService.getMetricByIdentify(VALID_STUB)
        }

        verify {
            metric.identify
        }

        Assertions.assertEquals(metric, metricByIdentify)
    }

    @Test
    fun `should throw an exception on getting the metric by invalid id`() {
        Assertions.assertThrows(MetricNotFoundException::class.java) {
            metricService.getMetricByIdentify(INVALID_STUB)
        }

        verify {
            metric.identify
        }
    }

    @Test
    fun `should return the formatted metrics`() {
        var formattedMetrics: String by Delegates.notNull()

        assertDoesNotThrow {
            formattedMetrics = metricService.getFormattedMetrics()
        }

        verify {
            metricService.getAllMetrics()
        }

        Assertions.assertEquals("${metric.title}: ${metric.description}", formattedMetrics)
    }
}