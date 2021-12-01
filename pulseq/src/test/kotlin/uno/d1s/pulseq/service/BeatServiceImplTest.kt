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
import uno.d1s.pulseq.configuration.ApplicationEventTestListenerConfiguration
import uno.d1s.pulseq.event.impl.DelayedBeatReceivedEvent
import uno.d1s.pulseq.exception.BeatNotFoundException
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.impl.BeatServiceImpl
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testDevice
import java.time.Duration
import java.util.*

@SpringBootTest
@ContextConfiguration(
    classes = [BeatServiceImpl::class, ApplicationEventTestListenerConfiguration::class]
)
internal class BeatServiceImplTest {

    @Autowired
    private lateinit var beatService: BeatServiceImpl

    @Autowired
    private lateinit var applicationEventTestListener: ApplicationEventTestListener

    @MockkBean
    private lateinit var beatRepository: BeatRepository

    @MockkBean
    private lateinit var deviceService: DeviceService

    @MockkBean
    private lateinit var activityService: ActivityService

    @BeforeEach
    fun setup() {
        every {
            beatRepository.findById(VALID_STUB)
        } returns Optional.of(testBeat)

        every {
            beatRepository.findById(INVALID_STUB)
        } returns Optional.empty()

        every {
            deviceService.findDeviceByIdentify(VALID_STUB)
        } returns testDevice

        every {
            deviceService.registerNewDevice(any())
        } returns testDevice

        every {
            activityService.isInactivityRelevanceLevelNotCommon()
        } returns true

        every {
            activityService.getCurrentInactivityDuration()
        } returns Duration.ZERO

        every {
            beatRepository.save(any())
        } returns testBeat

        every {
            beatRepository.findAllByDeviceIdEquals(VALID_STUB)
        } returns listOf()

        every {
            beatRepository.findAllByDeviceNameEqualsIgnoreCase(VALID_STUB)
        } returns listOf()

        every {
            beatRepository.findAllByDeviceNameEqualsIgnoreCaseOrDeviceIdEquals(VALID_STUB)
        } returns listOf()

        every {
            beatRepository.findAll()
        } returns listOf()
    }

    @Test
    fun `should return valid beat`() {
        assertDoesNotThrow {
            beatService.findBeatById(VALID_STUB)
        }

        verify {
            beatRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should throw an exception on getting beat with invalid id`() {
        Assertions.assertThrows(BeatNotFoundException::class.java) {
            beatService.findBeatById(INVALID_STUB)
        }

        verify {
            beatRepository.findById(INVALID_STUB)
        }
    }

    @Test
    fun `should register the beat with existing device`() {
        this.verifyBeatRegistrationPipelineCalls(VALID_STUB, false)
    }

    @Test
    fun `should register the beat with unresisting device`() {
        this.verifyBeatRegistrationPipelineCalls(INVALID_STUB, true)
    }

    @Test
    fun `should find all beats by device id`() {
        assertDoesNotThrow {
            beatService.findAllBeatsByDeviceId(VALID_STUB)
        }

        verify {
            beatRepository.findAllByDeviceIdEquals(VALID_STUB)
        }
    }

    @Test
    fun `should find all beats by device name`() {
        assertDoesNotThrow {
            beatService.findAllBeatsByDeviceName(VALID_STUB)
        }

        verify {
            beatRepository.findAllByDeviceNameEqualsIgnoreCase(VALID_STUB)
        }
    }

    @Test
    fun `should find all beats by device identify`() {
        assertDoesNotThrow {
            beatService.findAllBeatsByDeviceIdentify(VALID_STUB)
        }

        verify {
            beatRepository.findAllByDeviceNameEqualsIgnoreCaseOrDeviceIdEquals(VALID_STUB)
        }
    }

    @Test
    fun `should find all beats`() {
        assertDoesNotThrow {
            beatService.findAllBeats()
        }

        verify {
            beatRepository.findAll()
        }
    }

    @Test
    fun `should find total beats number`() {
        assertDoesNotThrow {
            beatService.totalBeats()
        }
    }

    @Test
    fun `should find total beats numbers by devices`() {
        assertDoesNotThrow {
            beatService.totalBeatsByDevices()
        }
    }

    @Test
    fun `should find last beat`() {
        this.makeBeatRepositoryNotEmpty()

        assertDoesNotThrow {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should throw an exception on finding last beat if there are no beats`() {
        Assertions.assertThrows(NoBeatsReceivedException::class.java) {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should find first beat`() {
        this.makeBeatRepositoryNotEmpty()

        assertDoesNotThrow {
            beatService.findFirstBeat()
        }
    }

    @Test
    fun `should throw an exception on finding first beat if there are no beats`() {
        Assertions.assertThrows(NoBeatsReceivedException::class.java) {
            beatService.findFirstBeat()
        }
    }

    private fun verifyBeatRegistrationPipelineCalls(deviceName: String, registerDevice: Boolean) {
        assertDoesNotThrow {
            beatService.registerNewBeatWithDeviceIdentify(deviceName)
        }

        if (registerDevice) {
            verify {
                deviceService.registerNewDevice(deviceName)
            }
        }

        verify {
            deviceService.findDeviceByIdentify(deviceName)
        }

        verify {
            activityService.getCurrentInactivityDuration()
        }

        verify {
            beatRepository.save(any())
        }

        verify {
            activityService.isInactivityRelevanceLevelNotCommon()
        }

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<DelayedBeatReceivedEvent>()
        )
    }

    private fun makeBeatRepositoryNotEmpty() {
        every {
            beatRepository.findAll()
        } returns listOf(testBeat)
    }
}