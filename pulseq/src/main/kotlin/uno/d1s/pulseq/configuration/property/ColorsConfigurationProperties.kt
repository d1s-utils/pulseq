package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.colors")
class ColorsConfigurationProperties(
    // see https://colorpicker.me/#ff4935, https://colorpicker.me/#ff7878
    var common: Int = 0xff7878,
    var warning: Int = 0xff4935
)