package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "pulseq.inactivity")
class InactivityConfigurationProperties(
    var common: Long
    = 5 * 1000 * 60 * 60.toLong(),
    var warning: Long
    = 24 * 1000 * 60 * 60.toLong()
)