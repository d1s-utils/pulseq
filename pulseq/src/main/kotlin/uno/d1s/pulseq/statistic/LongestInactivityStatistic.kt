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

    override val identify: String
        get() = TODO("Not yet implemented")

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

    override val shortDescription: String
        get() = TODO("Not yet implemented")
}