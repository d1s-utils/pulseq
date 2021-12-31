/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.exception.InvalidConfigurationException
import javax.annotation.PostConstruct

@ConfigurationProperties("pulseq.security")
class SecurityConfigurationProperties(
    var secret: String?
) {
    @PostConstruct
    private fun checkSecret() {
        secret ?: run {
            throw InvalidConfigurationException(
                "pulseq.security.secret"
            )
        }
    }
}