package uno.d1s.pulseq.client.configuration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan("uno.d1s.pulseq.client.configuration.properties")
class ConfigurationPropertiesConfiguration