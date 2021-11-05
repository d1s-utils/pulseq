package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "pulseq.inactivity")
class InactivityConfigurationProperties(
    var common: Duration = Duration.ofHours(5),
    var warning: Duration = Duration.ofDays(1)
)