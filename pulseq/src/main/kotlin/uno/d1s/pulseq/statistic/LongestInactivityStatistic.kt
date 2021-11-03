package uno.d1s.pulseq.statistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.InactivityStatusService
import uno.d1s.pulseq.util.pretty
import java.time.Duration

@Component
class LongestInactivityStatistic : Statistic {

    @Autowired
    private lateinit var inactivityStatusService: InactivityStatusService

    override val identify = "longest-inactivity"

    override val title = "Longest Inactivity"

    override val description
        get() = longestInactivity()

    override val shortDescription
        get() = longestInactivity()

    private fun longestInactivity() = runCatching {
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