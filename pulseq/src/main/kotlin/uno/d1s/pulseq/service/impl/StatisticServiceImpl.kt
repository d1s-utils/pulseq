package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.service.StatisticService
import uno.d1s.pulseq.statistic.Statistic

@Service("statisticService")
class StatisticServiceImpl : StatisticService {

    @Autowired
    private lateinit var statisticsCards: List<Statistic>

    override fun getAllStatistics(): List<Statistic> =
        statisticsCards

    override fun getStatisticByIdentify(identify: String): Statistic {
        TODO("Not yet implemented")
    }
}