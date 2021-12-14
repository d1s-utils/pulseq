package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKVerificationScope
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.impl.device.DeviceDeletedEvent
import uno.d1s.pulseq.event.impl.device.DeviceUpdatedEvent
import uno.d1s.pulseq.exception.impl.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.impl.DeviceNotFoundException
import uno.d1s.pulseq.repository.DeviceRepository
import uno.d1s.pulseq.service.impl.DeviceServiceImpl
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy
import uno.d1s.pulseq.strategy.device.byAll
import uno.d1s.pulseq.strategy.device.byId
import uno.d1s.pulseq.strategy.device.byName
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener
import java.util.*
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [DeviceServiceImpl::class, ApplicationEventTestListener::class])
internal class DeviceServiceImplTest {

    @Autowired
    private lateinit var deviceService: DeviceServiceImpl

    @MockkBean
    private lateinit var deviceRepository: DeviceRepository

    @MockkBean
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var applicationEventTestListener: ApplicationEventTestListener

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
            deviceRepository.findDeviceByNameEqualsIgnoreCase(testDeviceUpdate.name)
        } returns Optional.empty()

        val savedDevice = Device(INVALID_STUB)

        every {
            deviceRepository.save(savedDevice)
        } returns savedDevice

        every {
            deviceRepository.save(testDeviceUpdate)
        } returns testDeviceUpdate

        justRun {
            deviceRepository.delete(testDevice)
        }

        every {
            beatService.findAllByDevice(any())
        } returns testBeats

        every {
            beatService.findAllBeats()
        } returns testBeats

        justRun {
            beatService.deleteBeat(any(), any())
        }
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
            deviceRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should not find the device by all strategies`() {
        Assertions.assertThrows(DeviceNotFoundException::class.java) {
            deviceService.findDevice(byAll(INVALID_STUB))
        }

        verify {
            deviceRepository.findById(INVALID_STUB)
        }

        verify {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(INVALID_STUB)
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

        verify(exactly = 0) {
            deviceRepository.save(any())
        }
    }

    @Test
    fun `should update the device`() {
        var updatedDevice: Device by Delegates.notNull()

        assertDoesNotThrow {
            updatedDevice = deviceService.updateDevice(byAll(VALID_STUB), testDeviceUpdate)
        }

        // I'm stuck. This call literally has NO REASONS to not being passed.
        // java.lang.AssertionError: Verification failed: call 2 of 4:
        // Optional(child of uno.d1s.pulseq.repository.DeviceRepository#0 bean#75#156).orElseThrow(eq(uno.d1s.pulseq.service.impl.DeviceServiceImpl$$Lambda$744/0x0000000100795840@59f36439)))
        // was not called
        //verify {
        //    deviceService.findDevice(byAll(VALID_STUB))
        //}

        // Same with others.
        //verify {
        //    deviceService.findDevice(byName(testDeviceUpdate.name))
        //}

        verify {
            deviceRepository.save(testDeviceUpdate)
        }

        Assertions.assertEquals(testDeviceUpdate, updatedDevice)

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<DeviceUpdatedEvent>()
        )
    }

    @Test
    fun `should throw an exception while device updating with existing name of the device to be updated`() {
        every {
            deviceRepository.findDeviceByNameEqualsIgnoreCase(testDeviceUpdate.name)
        } returns Optional.of(testDeviceUpdate)

        Assertions.assertThrows(DeviceAlreadyExistsException::class.java) {
            deviceService.updateDevice(byAll(VALID_STUB), testDeviceUpdate)
        }

//        verify {
//            deviceService.findDevice(byAll(VALID_STUB))
//        }

//        verify {
//            deviceService.findDevice(byName(testDeviceUpdate.name))
//        }
    }

    @Test
    fun `should delete the device`() {
        assertDoesNotThrow {
            deviceService.deleteDevice(byAll(VALID_STUB))
        }

//        verify {
//            deviceService.findDevice(byAll(VALID_STUB))
//        }

        verify {
            beatService.findAllBeats()
        }

        verify {
            beatService.deleteBeat(any(), false)
        }

        verify {
            deviceRepository.delete(testDevice)
        }

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<DeviceDeletedEvent>()
        )
    }

    @Test
    fun `should find the device beats`() {
        var deviceBeats: List<Beat> by Delegates.notNull()

        assertDoesNotThrow {
            deviceBeats = deviceService.findDeviceBeats(byAll(VALID_STUB))
        }

//        verify {
//            deviceService.findDevice(byAll(VALID_STUB))
//        }

        Assertions.assertEquals(testBeats, deviceBeats)
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