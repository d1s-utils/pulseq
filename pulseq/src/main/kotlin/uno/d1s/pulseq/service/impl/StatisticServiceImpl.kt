package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.StatisticsConfigurationProperties
import uno.d1s.pulseq.exception.StatisticNotFoundException
import uno.d1s.pulseq.service.StatisticService
import uno.d1s.pulseq.statistic.Statistic
import uno.d1s.pulseq.util.getOrMessage

@Service("statisticService")
class StatisticServiceImpl : StatisticService {

    @Autowired
    private lateinit var statistics: List<Statistic>

    @Autowired
    private lateinit var statisticsConfigurationProperties: StatisticsConfigurationProperties

    override fun getAllStatistics(): List<Statistic> =
        statistics.filter { stat ->
            notExcluded(stat.identify)
        }

    override fun getStatisticByIdentify(identify: String): Statistic =
        statistics.firstOrNull {
            it.identify == identify && notExcluded(identify)
        } ?: throw StatisticNotFoundException("Provided identify is not valid or the statistic is disabled.")

    override fun getStatisticsFormatted(): String =
        runCatching {
            this.getAllStatistics().joinToString("\n") {
                "${it.title}: ${it.shortDescription}"
            }
        }.getOrMessage()

    private fun notExcluded(identify: String) =
        statisticsConfigurationProperties.exclude.none {
            it == identify
        }
}