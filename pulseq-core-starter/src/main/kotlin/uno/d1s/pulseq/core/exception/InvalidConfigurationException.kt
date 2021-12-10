package uno.d1s.pulseq.core.exception

class InvalidConfigurationException(
    val property: String, val action: String = "Make sure that property $property is present in the configuration."
) : RuntimeException(action)