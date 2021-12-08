package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.MetricsConfigurationProperties
import uno.d1s.pulseq.exception.impl.MetricNotFoundException
import uno.d1s.pulseq.service.MetricService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.getOrMessage

@Service
class MetricServiceImpl : MetricService {

    @Autowired
    private lateinit var metrics: List<Metric>

    @Autowired
    private lateinit var metricsConfigurationProperties: MetricsConfigurationProperties

    override fun getAllMetrics(): List<Metric> =
        metrics.filter { stat ->
            notExcluded(stat.identify)
        }

    override fun getMetricByIdentify(identify: String): Metric =
        metrics.firstOrNull {
            it.identify == identify && notExcluded(identify)
        } ?: throw MetricNotFoundException()

    override fun getFormattedMetrics(): String =
        runCatching {
            this.getAllMetrics().joinToString("\n") {
                "${it.title}: ${it.shortDescription}"
            }
        }.getOrMessage()

    private fun notExcluded(identify: String) =
        metricsConfigurationProperties.exclude.none {
            it == identify
        }
}