package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "pulseq.activity")
class ActivityConfigurationProperties(
    var long: Duration = Duration.ofHours(5),
    var warning: Duration = Duration.ofDays(1),
    var activityDelimiter: Duration = Duration.ofMinutes(30)
)