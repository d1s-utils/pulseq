package uno.d1s.pulseq.statistic.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.util.toCommaDelimitedString
import uno.d1s.pulseq.util.toSemicolonDelimitedString

@Component
class TotalBeatsByDevicesStatistic : Statistic {

    @Autowired
    private lateinit var beatService: BeatService

    override val identify = "total-beats-by-devices"

    override val title = "Total Beats By Devices"

    override val description
        get() = beatService.totalBeatsByDevices().map {
            "${it.key} - ${it.value}"
        }.toCommaDelimitedString(ErrorConstants.NO_BEATS_RECEIVED)

    override val shortDescription
        get() = beatService.totalBeatsByDevices().map {
            "${it.key}: ${it.value}"
        }.toSemicolonDelimitedString(ErrorConstants.NO_BEATS_RECEIVED)
}