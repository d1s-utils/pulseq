package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.statistics")
class StatisticsConfigurationProperties(
    var exclude: List<String> = listOf()
)