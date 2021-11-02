package uno.d1s.pulseq.client.configuration.properties

import org.apache.logging.log4j.LogManager
import org.springframework.boot.context.properties.ConfigurationProperties
import uno.d1s.pulseq.client.constant.KeyboardListeningModeConstants
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
    val logger = LogManager.getLogger()

    // TODO: 11/2/21
    // In server project I use custom FailureAnalyzer, and it should be likely user here as well.
    // I should write a starter for this all.
    @PostConstruct
    fun checkProperties() {
        val message: (prop: String) -> String = {
            "pulseq.client.$it can not be null, consider checking your configuration."
        }

        serverUrl ?: run {
            throw IllegalStateException(message("server-url"))
        }

        serverSecret ?: run {
            throw IllegalStateException(message("server-secret"))
        }
    }
}