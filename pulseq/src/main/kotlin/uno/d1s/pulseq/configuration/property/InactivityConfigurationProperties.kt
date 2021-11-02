package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.configuration.property.model.InactivityDurationPointConfigurationModel
import java.time.temporal.ChronoUnit

@ConfigurationProperties(prefix = "pulseq.inactivity")
class InactivityConfigurationProperties(
    var common: InactivityDurationPointConfigurationModel
    = InactivityDurationPointConfigurationModel(ChronoUnit.HOURS, 5),
    var warning: InactivityDurationPointConfigurationModel
    = InactivityDurationPointConfigurationModel(ChronoUnit.DAYS, 1)
)