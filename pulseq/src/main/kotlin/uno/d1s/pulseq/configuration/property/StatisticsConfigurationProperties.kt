package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.util.StringUtils

@ConfigurationProperties("pulseq.statistics")
class StatisticsConfigurationProperties(
    private var exclude: String? = null
) {

    fun parsedExcludes(): MutableSet<String> = StringUtils.commaDelimitedListToSet(exclude)
}