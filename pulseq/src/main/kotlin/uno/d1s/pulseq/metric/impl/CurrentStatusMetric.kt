package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.getOrMessage
import uno.d1s.pulseq.util.pretty

@Component
class CurrentStatusMetric : Metric {

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "current-status"

    override val title = "Current Status"

    override val description
        get() = runCatching {
            activityService.getCurrentTimeSpan().let { activity ->
                "${
                    activity.type.verb().replaceFirstChar {
                        it.uppercase()
                    }
                } for ${activity.duration.pretty()}. from ${activity.startBeat.beatTime.pretty()}"
            }
        }.getOrMessage()

    override val shortDescription
        get() = runCatching {
            activityService.getCurrentTimeSpan().type.verb()
        }.getOrMessage()
}