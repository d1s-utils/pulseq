package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.view")
class ViewConfigurationProperties(
    var enabled: Boolean = true,
    // see https://colorpicker.me/#ff7878
    var metaThemeColor: String = "#ff7878"
)