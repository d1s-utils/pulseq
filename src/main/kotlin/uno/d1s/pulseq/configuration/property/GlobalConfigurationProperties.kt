/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.exception.InvalidConfigurationException
import javax.annotation.PostConstruct

@ConfigurationProperties("pulseq")
class GlobalConfigurationProperties(
    var version: String?,
    var repository: String?,
    var serverName: String?
) {
    @PostConstruct
    private fun checkProperties() {
        version ?: throw InvalidConfigurationException(
            "pulseq.version"
        )

        repository ?: throw InvalidConfigurationException(
            "pulseq.repository"
        )

        serverName ?: throw InvalidConfigurationException(
            "pulseq.server-name"
        )
    }
}