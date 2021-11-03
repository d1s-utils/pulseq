package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("pulseq.view")
class ViewConfigurationProperties