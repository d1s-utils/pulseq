package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKVerificationScope
import io.mockk.called
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.exception.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.impl.DeviceServiceImpl
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy
import uno.d1s.pulseq.strategy.device.byAll
import uno.d1s.pulseq.strategy.device.byId
import uno.d1s.pulseq.strategy.device.byName
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testDevice
import uno.d1s.pulseq.testUtils.testDevices
import java.util.*
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [DeviceServiceImpl::class])
internal class DeviceServiceImplTest {

    @Autowired
    private lateinit var deviceService: DeviceServiceImpl

    @MockkBean
    private lateinit var deviceRepository: DeviceRepository

    private val optionalTestDevice = Optional.of(testDevice)

    @BeforeEach
    fun setup() {
        every {
            deviceRepository.findAll()
        } returns testDevices

        every {
            deviceRepository.findById(VALID_STUB)
        } returns optionalTestDevice

        every {
            deviceRepository.findById(INVALID_STUB)
        } returns Optional.empty()

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        } returns optionalTestDevice

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        } returns Optional.empty()

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCaseOrIdEquals(VALID_STUB)
        } returns optionalTestDevice

        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCaseOrIdEquals(INVALID_STUB)
        } returns Optional.empty()

        val savedDevice = Device(INVALID_STUB)

        every {
            deviceRepository.save(savedDevice)
        } returns savedDevice
    }

    @Test
    fun `should find all registered devices`() {
        var all: List<Device> by Delegates.notNull()

        assertDoesNotThrow {
            all = deviceService.findAllRegisteredDevices()
        }

        verify {
            deviceRepository.findAll()
        }

        Assertions.assertEquals(testDevices, all)
    }

    @Test
    fun `should find the device by id`() {
        this.findDeviceAndVerify(byId(VALID_STUB)) {
            deviceRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should not find the device by id`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDevice(byId(INVALID_STUB))
        }

        verify {
            deviceRepository.findById(INVALID_STUB)
        }
    }

    @Test
    fun `should find the device by name`() {
        this.findDeviceAndVerify(byName(VALID_STUB)) {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        }
    }

    @Test
    fun `should not find th device by name`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDevice(byName(INVALID_STUB))
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        }
    }

    @Test
    fun `should find the device by all strategies`() {
        this.findDeviceAndVerify(byAll(VALID_STUB)) {
            deviceRepository.findDeviceByNameEqualsIgnoreCaseOrIdEquals(VALID_STUB)
        }
    }

    @Test
    fun `should not find the device by all strategies`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDevice(byAll(INVALID_STUB))
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCaseOrIdEquals(INVALID_STUB)
        }
    }

    @Test
    fun `should register the device`() {
        var newDevice: Device by Delegates.notNull()

        assertDoesNotThrow {
            newDevice = deviceService.registerNewDevice(INVALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
        }

        verify {
            deviceRepository.save(any())
        }

        Assertions.assertEquals(INVALID_STUB, newDevice.name)
    }

    @Test
    fun `should throw an exception on device registration with existing name`() {
        Assertions.assertThrows(DeviceAlreadyExistsException::class.java) {
            deviceService.registerNewDevice(VALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(VALID_STUB)
        }

        verify {
            deviceRepository.save(any()) wasNot called
        }
    }

    private fun findDeviceAndVerify(strategy: DeviceFindingStrategy, verifications: MockKVerificationScope.() -> Unit) {
        var device: Device by Delegates.notNull()

        assertDoesNotThrow {
            device = deviceService.findDevice(strategy)
        }

        verify(verifyBlock = verifications)
        Assertions.assertEquals(device, testDevice)
    }
}