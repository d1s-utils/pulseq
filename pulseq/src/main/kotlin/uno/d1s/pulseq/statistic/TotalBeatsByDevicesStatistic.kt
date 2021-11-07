package uno.d1s.pulseq.statistic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.service.BeatService

@Component
class TotalBeatsByDevicesStatistic : Statistic {

    @Autowired
    private lateinit var beatService: BeatService

    override val identify = "total-beats-by-devices"

    override val title = "Total Beats By Devices"

    override val description: String
        get() = build(false) {
            "${it.key} - ${it.value}"
        }

    override val shortDescription: String
        get() = build(true) {
            "${it.key}: ${it.value};"
        }

    private fun build(short: Boolean, appending: (entry: Map.Entry<String, Int>) -> String) =
        buildString {
            val total = beatService.totalBeatsByDevices()

            total.forEach {
                append(
                    "${appending(it)}${
                        if (it == total.entries.last()) {
                            ""
                        } else {
                            if (short) {
                                " "
                            } else {
                                ", "
                            }
                        }
                    }"
                )
            }
        }
}