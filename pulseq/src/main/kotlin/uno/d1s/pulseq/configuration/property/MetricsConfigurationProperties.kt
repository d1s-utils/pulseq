package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.metrics")
class MetricsConfigurationProperties(
    var exclude: List<String> = listOf()
)