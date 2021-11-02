package uno.d1s.pulseq.client.service.impl

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.client.configuration.properties.ClientConfigurationProperties
import uno.d1s.pulseq.client.service.BeatSenderService

@Service
class BeatSenderServiceImpl : BeatSenderService {

    private val logger = LogManager.getLogger()

    @Autowired
    private lateinit var clientConfigurationProperties: ClientConfigurationProperties

    @Autowired
    private lateinit var httpClient: HttpClient

    override fun sendBeat() {
        CoroutineScope(Dispatchers.Default).launch {
            logger.info("Sending beat from device ${clientConfigurationProperties.deviceName} to ${clientConfigurationProperties.serverUrl!!}")
            httpClient.post<HttpResponse>(
                clientConfigurationProperties.serverUrl!!.let {
                    if (it.endsWith("/")) {
                        it
                    } else {
                        "$it/"
                    }
                } + "api/beat/"
            ) {
                parameter("device", clientConfigurationProperties.deviceName)
                header("Authorization", clientConfigurationProperties.serverSecret)
            }.let { response ->
                logger.info("Response status: ${response.status.value}; Body:\n${response.content.readUTF8Line()}")
            }
        }
    }
}