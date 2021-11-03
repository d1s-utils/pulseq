package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.badge")
class BadgeConfigurationProperties(
    // see https://colorpicker.me/#ff7878
    var color: String =
        "ff7878"
)