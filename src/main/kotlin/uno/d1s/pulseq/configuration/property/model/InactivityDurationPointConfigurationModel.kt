package uno.d1s.pulseq.configuration.property.model

import java.time.temporal.ChronoUnit

class InactivityDurationPointConfigurationModel(
    var unit: ChronoUnit = ChronoUnit.HOURS,
    var value: Long = 5
)