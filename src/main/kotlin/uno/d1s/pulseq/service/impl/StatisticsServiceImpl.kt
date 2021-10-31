package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.model.statistics.StatisticsCard
import uno.d1s.pulseq.service.StatisticsService

@Service("statisticsService")
class StatisticsServiceImpl : StatisticsService {

    @Autowired
    private lateinit var statisticsCards: List<StatisticsCard>

    override fun getAllStatisticsCards(): List<StatisticsCard> =
        statisticsCards
}