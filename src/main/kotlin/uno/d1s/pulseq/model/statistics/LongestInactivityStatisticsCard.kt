package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.InactivityStatusService
import uno.d1s.pulseq.util.pretty
import java.time.Duration

@Component
class LongestInactivityStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var inactivityStatusService: InactivityStatusService

    override val title = "Longest Recorded Inactivity"

    override val description
        get() = runCatching {
            inactivityStatusService.getLongestInactivity().let { inactivity ->
                if (inactivity == Duration.ZERO) {
                    "No inactivity information available."
                } else {
                    inactivity.pretty()
                }
            }
        }.getOrElse {
            it.message!!
        }
}