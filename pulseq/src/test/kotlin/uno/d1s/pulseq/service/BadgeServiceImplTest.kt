package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import uno.d1s.pulseq.configuration.property.BadgeConfigurationProperties
import uno.d1s.pulseq.service.impl.BadgeServiceImpl
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testMetric

@SpringBootTest
@ContextConfiguration(classes = [BadgeServiceImpl::class])
internal class BadgeServiceImplTest {

    @Autowired
    private lateinit var badgeService: BadgeServiceImpl

    @MockkBean
    private lateinit var restTemplate: RestTemplate

    @MockkBean(relaxed = true)
    private lateinit var badgeConfigurationProperties: BadgeConfigurationProperties

    @MockkBean
    private lateinit var metricService: MetricService

    @BeforeEach
    fun setup() {
        every {
            restTemplate.getForObject<ByteArray>(any<String>())
        } returns byteArrayOf()

        every {
            metricService.getMetricByIdentify(VALID_STUB)
        } returns testMetric
    }

    @Test
    fun `should return valid badge`() {
        assertDoesNotThrow {
            badgeService.createBadge(VALID_STUB, VALID_STUB, VALID_STUB, VALID_STUB)
        }

        verify {
            metricService.getMetricByIdentify(VALID_STUB)
        }

        verify {
            restTemplate.getForObject<ByteArray>(any<String>())
        }
    }
}