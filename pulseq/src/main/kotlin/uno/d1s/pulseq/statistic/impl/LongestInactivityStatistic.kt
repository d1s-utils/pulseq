package uno.d1s.pulseq.statistic.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.util.getOrMessage
import uno.d1s.pulseq.util.pretty

@Component
class LongestInactivityStatistic : Statistic {

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "longest-inactivity"

    override val title = "Longest Inactivity"

    override val description
        get() = runCatching {
            activityService.getLongestInactivity().duration.pretty()
        }.getOrMessage()

    override val shortDescription
        get() = this.description
}