package uno.d1s.pulseq.metric.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.metric.Metric
import uno.d1s.pulseq.util.pretty

@Component
class TotalBeatsMetric : Metric {

    @Autowired
    private lateinit var beatService: BeatService

    override val identify = "total-beats"

    override val title = "Total Beats"

    override val description
        get() = totalSafe {
            "$it beats was received since ${beatService.findFirstBeat().beatTime.pretty()}."
        }

    override val shortDescription
        get() = totalSafe {
            it.toString()
        }

    private inline fun totalSafe(message: (total: Int) -> String) = beatService.totalBeats().let { total ->
        if (total <= 0) {
            ErrorConstants.NO_BEATS_RECEIVED
        } else {
            message(total)
        }
    }
}