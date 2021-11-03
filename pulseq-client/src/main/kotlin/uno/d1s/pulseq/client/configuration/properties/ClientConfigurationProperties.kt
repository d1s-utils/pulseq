package uno.d1s.pulseq.client.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.core.exception.InvalidConfigurationException
import javax.annotation.PostConstruct

@ConfigurationProperties("pulseq.client")
class ClientConfigurationProperties(
    var serverUrl: String?,
    var serverSecret: String?,
    var deviceName: String =
        System.getProperty("os.name"),
) {

    @PostConstruct
    fun checkProperties() {
        serverUrl ?: run {
            throw InvalidConfigurationException("pulseq.client.server-url")
        }

        serverSecret ?: run {
            throw InvalidConfigurationException("pulseq.client.server-secret")
        }
    }
}