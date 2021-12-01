package uno.d1s.pulseq.statistic

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.statistic.impl.DevicesStatistic
import uno.d1s.pulseq.util.assertNoWhitespace

@SpringBootTest
@ContextConfiguration(classes = [DevicesStatistic::class])
internal class DevicesStatisticTest {

    @Autowired
    private lateinit var devicesStatistic: DevicesStatistic

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
            devicesStatistic.identify
        }

        devicesStatistic.identify.assertNoWhitespace()
    }

    @Test
    fun `should return title`() {
        assertDoesNotThrow {
            devicesStatistic.title
        }
    }

    @Test
    fun `should return description`() {
        assertDoesNotThrow {
            devicesStatistic.description
        }

        verify {
            deviceService.findAllRegisteredDevices()
        }
    }

    @Test
    fun `should return short description`() {
        assertDoesNotThrow {
            devicesStatistic.shortDescription
        }

        verify {
            deviceService.findAllRegisteredDevices()
        }
    }
}