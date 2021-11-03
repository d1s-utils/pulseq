package uno.d1s.pulseq.client.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.client.configuration.properties.model.CountDownLatchConfigurationModel

@ConfigurationProperties("pulseq.client.keyboard-listening-mode")
class KeyboardListeningModeConfigurationProperties(
    val countDownLatch: CountDownLatchConfigurationModel
    = CountDownLatchConfigurationModel()
)