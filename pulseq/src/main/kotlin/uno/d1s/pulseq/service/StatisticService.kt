package uno.d1s.pulseq.service

import uno.d1s.pulseq.statistic.Statistic

interface StatisticService {

    fun getAllStatistics(): List<Statistic>

    fun getStatisticByIdentify(identify: String): Statistic

    fun getStatisticsFormatted(): String
}