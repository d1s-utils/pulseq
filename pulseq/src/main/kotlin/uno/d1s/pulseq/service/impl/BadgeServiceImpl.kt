package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import uno.d1s.pulseq.configuration.property.ServerConfigurationProperties
import uno.d1s.pulseq.service.BadgeService
import uno.d1s.pulseq.statistic.Statistic

@Service
class BadgeServiceImpl : BadgeService {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var serverConfigurationProperties: ServerConfigurationProperties

    override fun createBadge(statistic: Statistic): Array<Byte> =
        restTemplate.getForObject("https://shields.io/badge/", Array<Byte>::class)
}