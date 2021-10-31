package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.exception.InvalidConfigurationException
import javax.annotation.PostConstruct

@ConfigurationProperties("pulseq")
class GlobalConfigurationProperties(
    var version: String?,
    var repository: String?
) {
    @PostConstruct
    private fun checkProperties() {
        version ?: throw InvalidConfigurationException(
            "pulseq.version can not be null."
        )

        repository ?: throw InvalidConfigurationException(
            "pulseq.repository can not be null."
        )
    }
}