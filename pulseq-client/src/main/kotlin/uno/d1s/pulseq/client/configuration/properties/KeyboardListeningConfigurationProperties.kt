package uno.d1s.pulseq.client.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.client.configuration.properties.model.CountDownLatchConfigurationModel
import uno.d1s.pulseq.client.configuration.properties.model.TimerConfigurationModel

@ConfigurationProperties("pulseq.client.keyboard-listening-mode")
class KeyboardListeningConfigurationProperties(
    val countDownLatch: CountDownLatchConfigurationModel
    = CountDownLatchConfigurationModel(),
    val timer: TimerConfigurationModel
    = TimerConfigurationModel()
)