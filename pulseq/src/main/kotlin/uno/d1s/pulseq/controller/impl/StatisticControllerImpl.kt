package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.StatisticController
import uno.d1s.pulseq.service.StatisticService
import uno.d1s.pulseq.statistic.Statistic

@RestController
class StatisticControllerImpl : StatisticController {

    @Autowired
    private lateinit var statisticService: StatisticService

    override fun getAllStatistics(): ResponseEntity<List<Statistic>> =
        ResponseEntity.ok(
            statisticService.getAllStatistics()
        )

    override fun getStatisticByIdentify(identify: String): ResponseEntity<Statistic> =
        ResponseEntity.ok(
            statisticService.getStatisticByIdentify(identify)
        )
}