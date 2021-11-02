package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.exception.StatisticNotFoundException
import uno.d1s.pulseq.service.StatisticService
import uno.d1s.pulseq.statistic.Statistic

@Service("statisticService")
class StatisticServiceImpl : StatisticService {

    @Autowired
    private lateinit var statistics: List<Statistic>

    override fun getAllStatistics(): List<Statistic> =
        statistics

    override fun getStatisticByIdentify(identify: String): Statistic =
        statistics.firstOrNull { it.identify == identify }
            ?: throw StatisticNotFoundException("Provided identify is not valid.")
}