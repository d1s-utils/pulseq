package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService

@Component
class TotalBeatsStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var beatService: BeatService

    override val title: String
        get() = "Total Beats"

    override val description: String
        get() = "${beatService.totalBeats()} beats was received for all time."
}