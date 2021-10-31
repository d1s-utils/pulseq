package uno.d1s.pulseq.service

import uno.d1s.pulseq.model.statistics.StatisticsCard

interface StatisticsService {

    fun getAllStatisticsCards(): List<StatisticsCard>
}