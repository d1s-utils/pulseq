package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.InactivityStatusService

@Component
class LastBeatStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var inactivityStatusService: InactivityStatusService

    override val title = "Last Beat"

    override val description
        get() = runCatching {
            val lastBeat = beatService.findLastBeat()
            "Last beat with id ${lastBeat.id} was received ${inactivityStatusService.getCurrentInactivityPretty()} ago from device ${lastBeat.device.name}."
        }.getOrElse {
            it.message!!
        }
}