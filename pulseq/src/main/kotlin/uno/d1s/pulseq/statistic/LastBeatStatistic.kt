package uno.d1s.pulseq.statistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.ActivityService

@Component
class LastBeatStatistic : Statistic {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityService: ActivityService

    override val identify = "last-beat"

    override val title = "Last Beat"

    override val description
        get() = runCatching {
            val lastBeat = beatService.findLastBeat()
            "Last beat with id ${lastBeat.id} was received ${activityService.getCurrentInactivityPretty()} ago from device ${lastBeat.device.name}."
        }.getOrElse {
            it.message!!
        }

    override val shortDescription
        get() = runCatching {
            "${activityService.getCurrentInactivityPretty()} ago"
        }.getOrElse {
            it.message!!
        }
}