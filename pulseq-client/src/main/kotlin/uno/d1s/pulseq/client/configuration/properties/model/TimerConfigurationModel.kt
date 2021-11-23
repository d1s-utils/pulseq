package uno.d1s.pulseq.client.configuration.properties.model

import java.time.Duration

class TimerConfigurationModel(
    override var enabled: Boolean = false,
    var delay: Duration = Duration.ofMinutes(1)
) : KeyboardListeningMode