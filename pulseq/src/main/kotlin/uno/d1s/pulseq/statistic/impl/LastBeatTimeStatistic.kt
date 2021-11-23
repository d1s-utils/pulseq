package uno.d1s.pulseq.statistic.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.util.getOrMessage
import uno.d1s.pulseq.util.pretty

@Component
class LastBeatTimeStatistic : Statistic {

    @Autowired
    private lateinit var beatService: BeatService

    override val identify = "last-beat-time"

    override val title = "Last Beat Time"

    override val description
        get() = runCatching {
            "Last beat was received at ${formatted()}."
        }.getOrMessage()

    override val shortDescription
        get() = runCatching {
            formatted()
        }.getOrMessage()

    private fun formatted() =
        beatService.findLastBeat().beatTime.pretty()
}