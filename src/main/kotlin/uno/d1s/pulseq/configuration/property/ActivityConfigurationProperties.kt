/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "pulseq.activity")
class ActivityConfigurationProperties(
    var activityDelimiter: Duration = Duration.ofMinutes(30)
)