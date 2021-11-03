package uno.d1s.pulseq.client.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.client.constant.KeyboardListeningModeConstants
import uno.d1s.pulseq.core.exception.InvalidConfigurationException
import javax.annotation.PostConstruct

@ConfigurationProperties("pulseq.client")
class ClientConfigurationProperties(
    var serverUrl: String?,
    var serverSecret: String?,
    var deviceName: String =
        System.getProperty("os.name"),
    var keyboardListeningMode: String =
        KeyboardListeningModeConstants.COUNT_DOWN_LATCH

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