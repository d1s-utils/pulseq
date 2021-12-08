package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.getOrMessage
import uno.d1s.pulseq.util.pretty

@Component
class LastBeatMetric : Metric {

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "last-beat"

    override val title = "Last Beat"

    override val description
        get() = runCatching {
            val activity = activityService.getCurrentTimeSpan()
            "Last beat with id ${activity.startBeat.id} was received ${activity.duration.pretty()} ago from device ${activity.startBeat.device.name}."
        }.getOrMessage()

    override val shortDescription
        get() = runCatching {
            "${activityService.getCurrentInactivityPretty()} ago"
        }.getOrMessage()
}