package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService

@Component
class TotalBeatsStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var beatService: BeatService

    override val title = "Total Beats"

    override val description
        get() = beatService.totalBeats().let { total ->
            if (total <= 0) {
                "No beats received yet."
            } else {
                "${beatService.totalBeats()} beats was received for all time."
            }
        }
}