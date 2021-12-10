package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.MetricController
import uno.d1s.pulseq.service.MetricService
import uno.d1s.pulseq.metric.Metric

@RestController
class MetricControllerImpl : MetricController {

    @Autowired
    private lateinit var metricService: MetricService

    override fun getAllMetrics(): ResponseEntity<List<Metric>> =
        ResponseEntity.ok(
            metricService.getAllMetrics()
        )

    override fun getMetricByIdentify(identify: String): ResponseEntity<Metric> =
        ResponseEntity.ok(
            metricService.getMetricByIdentify(identify)
        )
}