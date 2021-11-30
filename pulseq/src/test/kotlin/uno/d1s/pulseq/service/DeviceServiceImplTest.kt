package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.exception.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.impl.DeviceServiceImpl
import uno.d1s.pulseq.util.INVALID_STUB
import uno.d1s.pulseq.util.VALID_STUB
import uno.d1s.pulseq.util.testDevice
import java.util.*

@SpringBootTest
@ContextConfiguration(classes = [DeviceServiceImpl::class])
internal class DeviceServiceImplTest {

    @Autowired
    private lateinit var deviceService: DeviceServiceImpl

    @MockkBean
    private lateinit var deviceRepository: DeviceRepository

    @BeforeEach
    fun setup() {
        every {
            deviceRepository.findAll()
        } returns listOf()

        every {
            deviceRepository.findById(VALID_STUB)
        } returns Optional.of(testDevice)

        every {
            deviceRepository.findById(INVALID_STUB)
        } returns Optional.empty()

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        } returns Optional.of(testDevice)

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        } returns Optional.empty()

        every {
            deviceRepository.save(any())
        } returns testDevice
    }

    @Test
    fun `should find all registered devices`() {
        assertDoesNotThrow {
            deviceService.findAllRegisteredDevices()
        }

        verify {
            deviceRepository.findAll()
        }
    }

    @Test
    fun `should find the device by id`() {
        assertDoesNotThrow {
            deviceService.findDeviceById(VALID_STUB)
        }

        verify {
            deviceRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should throw an exception on finding the device by invalid id`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDeviceById(INVALID_STUB)
        }

        verify {
            deviceRepository.findById(INVALID_STUB)
        }
    }

    @Test
    fun `should find the device by name`() {
        assertDoesNotThrow {
            deviceService.findDeviceByName(VALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        }
    }

    @Test
    fun `should throw an exception on finding the device by invalid name`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDeviceByName(INVALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        }
    }

    @Test
    fun `should find the device by identify`() {
        assertDoesNotThrow {
            deviceService.findDeviceByIdentify(VALID_STUB)
        }
    }

    @Test
    fun `should throw an exception on finding the beat by invalid identify`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDeviceByIdentify(INVALID_STUB)
        }
    }

    @Test
    fun `should register the device`() {
        assertDoesNotThrow {
            deviceService.registerNewDevice(INVALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        }

        verify {
            deviceRepository.save(any())
        }
    }

    @Test
    fun `should throw an exception on device registration with existing name`() {
        Assertions.assertThrows(DeviceAlreadyExistsException::class.java) {
            deviceService.registerNewDevice(VALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        }
    }
}