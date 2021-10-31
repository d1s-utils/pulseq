package uno.d1s.pulseq.configuration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(
    basePackages = ["uno.d1s.pulseq.configuration.property",
        "uno.d1s.pulseq.configuration.property.notifications"]
)
class ConfigurationPropertiesConfiguration