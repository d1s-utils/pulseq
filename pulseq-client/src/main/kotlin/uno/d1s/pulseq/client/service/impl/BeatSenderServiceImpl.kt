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
import uno.d1s.pulseq.core.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.client.service.BeatSenderService
import uno.d1s.pulseq.core.util.buildUrl
import uno.d1s.pulseq.core.util.withSlash

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
                buildUrl(clientConfigurationProperties.serverUrl!!.withSlash()) {
                    path(BeatMappingConstants.BASE)
                }
            ) {
                parameter("device", clientConfigurationProperties.deviceName)
                header("Authorization", clientConfigurationProperties.serverSecret)
            }.let { response ->
                logger.info("Response status: ${response.status.value}; Body:\n${response.content.readUTF8Line()}")
            }
        }
    }
}