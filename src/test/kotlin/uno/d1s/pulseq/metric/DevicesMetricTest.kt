package uno.d1s.pulseq.metric

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.metric.impl.DevicesMetric
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.testUtils.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [DevicesMetric::class])
internal class DevicesMetricTest {

    @Autowired
    private lateinit var devicesMetric: DevicesMetric

    @MockkBean
    private lateinit var deviceService: DeviceService

    @BeforeEach
    fun setup() {
        every {
            deviceService.findAllRegisteredDevices()
        } returns listOf()
    }

    @Test
    fun `should return valid identify`() {
        assertDoesNotThrow {
            devicesMetric.identify
        }

        devicesMetric.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            devicesMetric.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            devicesMetric.description
        }

        verify {
            deviceService.findAllRegisteredDevices()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            devicesMetric.shortDescription
        }

        verify {
            deviceService.findAllRegisteredDevices()
        }
    }
}