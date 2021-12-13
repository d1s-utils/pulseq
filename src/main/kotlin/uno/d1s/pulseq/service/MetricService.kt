package uno.d1s.pulseq.service

import uno.d1s.pulseq.metric.Metric

interface MetricService {

    fun getAllMetrics(): List<Metric>

    fun getMetricByIdentify(identify: String): Metric

    fun getFormattedMetrics(): String
}