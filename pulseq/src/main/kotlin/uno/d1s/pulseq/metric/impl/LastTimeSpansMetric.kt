package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.pretty
import uno.d1s.pulseq.util.toSemicolonDelimitedString

@Component
class LastTimeSpansMetric : Metric {

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "last-time-spans"

    override val title = "Last Time Spans"

    override val description
        get() = runCatching {
            activityService.getAllTimeSpans().let { all ->
                val last = all.indexOf(all.last())

                // TODO: 16.11.2021 make this configurable
                all.subList(last - 4, last + 1).mapped()
            }
        }.getOrElse {
            if (it is IndexOutOfBoundsException) {
                activityService.getAllTimeSpans().mapped()
            } else {
                listOf()
            }
        }.toSemicolonDelimitedString(ErrorConstants.TIME_SPANS_NOT_AVAILABLE)

    override val shortDescription
        get() = this.description

    private fun List<TimeSpan>.mapped() = this.map {
        "${it.duration.pretty()} of ${it.type.name.lowercase()}"
    }.reversed()
}