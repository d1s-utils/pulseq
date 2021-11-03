package uno.d1s.pulseq.client.configuration.properties.model

class CountDownLatchConfigurationModel(
    override val enabled: Boolean = false,
    val countTrigger: Int = 5
) : KeyboardListeningMode