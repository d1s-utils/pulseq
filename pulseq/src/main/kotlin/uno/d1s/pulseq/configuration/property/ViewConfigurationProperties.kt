package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("pulseq.view")
class ViewConfigurationProperties(
    var enabled: Boolean = true
)