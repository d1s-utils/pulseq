package uno.d1s.pulseq.client.configuration.properties.model

class CountDownLatchConfigurationModel(
    override var enabled: Boolean = false,
    var countTrigger: Int = 5
) : KeyboardListeningMode