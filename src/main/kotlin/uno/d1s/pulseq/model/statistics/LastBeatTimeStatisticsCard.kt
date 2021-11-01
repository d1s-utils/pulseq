package uno.d1s.pulseq.model.statistics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Component
class LastBeatTimeStatisticsCard : StatisticsCard {

    @Autowired
    private lateinit var beatService: BeatService

    override val title = "Last Beat Time"

    override val description
        get() = runCatching {
            "Last beat was received at ${
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
                    .withZone(ZoneId.of("UTC")).format(beatService.findLastBeat().beatTime)
            }"
        }.getOrElse {
            it.message!!
        }
}