package uno.d1s.pulseq.statistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Component
class LastBeatTimeStatistic : Statistic {

    @Autowired
    private lateinit var beatService: BeatService

    override val identify = "last-beat-time"

    override val title = "Last Beat Time"

    override val description
        get() = runCatching {
            "Last beat was received at ${formattedTime()}"
        }.getOrElse {
            it.message!!
        }

    override val shortDescription: String
        get() = runCatching {
            formattedTime()
        }.getOrElse {
            it.message!!
        }

    private fun formattedTime() = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
        .withZone(ZoneId.of("UTC")).format(beatService.findLastBeat().beatTime)
}