package uno.d1s.pulseq.service.impl

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import uno.d1s.pulseq.configuration.property.BadgeConfigurationProperties
import uno.d1s.pulseq.core.util.buildUrl
import uno.d1s.pulseq.exception.impl.InvalidImageUrlException
import uno.d1s.pulseq.service.BadgeService
import uno.d1s.pulseq.service.MetricService
import java.net.URL
import java.util.*

@Service
class BadgeServiceImpl : BadgeService {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var badgeConfigurationProperties: BadgeConfigurationProperties

    @Autowired
    private lateinit var metricService: MetricService

    override fun createBadge(
        metricId: String,
        color: String?,
        title: String?,
        style: String?,
        logoUrl: String?
    ): ByteArray =
        metricService.getMetricByIdentify(metricId).let { stat ->
            restTemplate.getForObject(
                buildUrl(
                    "https://shields.io/badge/${
                        if (StringUtils.hasText(title)) {
                            title!!
                        } else {
                            stat.title
                        }
                    }-${stat.shortDescription}-${badgeConfigurationProperties.defaultColor}"
                ) {
                    if (StringUtils.hasText(color)) {
                        parameter("color", color!!)
                    }

                    parameter(
                        "color",
                        if (StringUtils.hasText(color)) {
                            color!!
                        } else {
                            badgeConfigurationProperties.defaultColor
                        }
                    )

                    if (StringUtils.hasText(style)) {
                        parameter("style", style!!)
                    }

                    if (StringUtils.hasText(logoUrl)) {
                        parameter(
                            "logo",
                            "data:image/png;base64,${
                                try {
                                    Base64.getEncoder().encodeToString(IOUtils.toByteArray(URL(logoUrl!!))).let {
                                        if (it.length > 8000) {
                                            throw InvalidImageUrlException
                                        } else {
                                            it
                                        }
                                    }
                                } catch (ex: Exception) {
                                    throw InvalidImageUrlException
                                }
                            }"
                        )
                    }
                }
            )
        }
}