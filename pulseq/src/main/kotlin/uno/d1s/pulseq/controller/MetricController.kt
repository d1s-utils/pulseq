package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.core.constant.mapping.MetricMappingConstants
import uno.d1s.pulseq.metric.Metric
import javax.validation.constraints.NotEmpty

@Validated
interface MetricController {

    @GetMapping(MetricMappingConstants.BASE)
    fun getAllMetrics(): ResponseEntity<List<Metric>>

    @GetMapping(MetricMappingConstants.GET_METRIC_BY_IDENTIFY)
    fun getMetricByIdentify(@PathVariable @NotEmpty identify: String): ResponseEntity<Metric>
}