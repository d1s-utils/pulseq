package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import uno.d1s.pulseq.configuration.property.BadgeConfigurationProperties
import uno.d1s.pulseq.service.BadgeService
import uno.d1s.pulseq.service.StatisticService

@Service("badgeService")
class BadgeServiceImpl : BadgeService {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var badgeConfigurationProperties: BadgeConfigurationProperties

    @Autowired
    private lateinit var statisticService: StatisticService

    override fun createBadge(statisticId: String): ByteArray =
        statisticService.getStatisticByIdentify(statisticId).let { stat ->
            restTemplate.getForObject(
                "https://raster.shields.io/badge/${stat.title}-${stat.shortDescription}-${badgeConfigurationProperties.color}",
                ByteArray::class
            )
        }
}