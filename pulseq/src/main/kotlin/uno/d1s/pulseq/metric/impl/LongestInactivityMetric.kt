package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.getOrMessage
import uno.d1s.pulseq.util.pretty

@Component
class LongestInactivityMetric : Metric {

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "longest-inactivity"

    override val title = "Longest Inactivity"

    override val description
        get() = runCatching {
            activityService.getLongestTimeSpan().duration.pretty()
        }.getOrMessage()

    override val shortDescription
        get() = this.description
}