package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
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
import uno.d1s.pulseq.configuration.ApplicationEventTestListenerConfiguration
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.impl.beat.BeatDeletedEvent
import uno.d1s.pulseq.event.impl.beat.BeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.DeviceNotFoundException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.impl.BeatServiceImpl
import uno.d1s.pulseq.strategy.device.*
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.properties.Delegates

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
            deviceService.registerNewDevice(VALID_STUB)
        } returns testDevice

        every {
            deviceService.registerNewDevice(INVALID_STUB)
        } returns Device(INVALID_STUB)

        every {
            deviceService.findDevice(byAll(VALID_STUB))
        } returns testDevice

        every {
            deviceService.findDevice(byAll(INVALID_STUB))
        } throws DeviceNotFoundException()

        every {
            deviceService.findDevice(byId(VALID_STUB))
        } returns testDevice

        every {
            deviceService.findDevice(byName(VALID_STUB))
        } returns testDevice

        every {
            deviceService.findDevice(byName(INVALID_STUB))
        } throws DeviceNotFoundException() andThen Device(INVALID_STUB)

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
            beatRepository.findAll()
        } returns testBeats

        justRun {
            beatRepository.delete(testBeat)
        }
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
    fun `should register the beat with non existent device`() {
        this.verifyBeatRegistrationPipelineCalls(INVALID_STUB, true)
    }

    @Test
    fun `should register the beat with existing device`() {
        this.verifyBeatRegistrationPipelineCalls(VALID_STUB, false)
    }

    @Test
    fun `should find all beats by device id`() {
        this.findAllByDeviceAndAssert(DeviceFindingStrategyType.BY_ID)
    }

    @Test
    fun `should find all beats by device name`() {
        this.findAllByDeviceAndAssert(DeviceFindingStrategyType.BY_NAME)
    }

    @Test
    fun `should find all beats by device identify`() {
        this.findAllByDeviceAndAssert(DeviceFindingStrategyType.BY_ALL)
    }

    @Test
    fun `should find all beats`() {
        var all: List<Beat> by Delegates.notNull()

        assertDoesNotThrow {
            all = beatService.findAllBeats()
        }

        verify {
            beatRepository.findAll()
        }

        Assertions.assertEquals(testBeats, all)
    }

    @Test
    fun `should find total beats number`() {
        var total: Int by Delegates.notNull()

        assertDoesNotThrow {
            total = beatService.totalBeats()
        }

        Assertions.assertEquals(testBeats.size, total)
    }

    @Test
    fun `should find total beats numbers by devices`() {
        var beatsByDevices: Map<String, Int> by Delegates.notNull()

        assertDoesNotThrow {
            beatsByDevices = beatService.totalBeatsByDevices()
        }

        Assertions.assertEquals(
            mapOf(
                VALID_STUB to 1
            ),
            beatsByDevices
        )
    }

    @Test
    fun `should find last beat`() {
        var lastBeat: Beat by Delegates.notNull()

        assertDoesNotThrow {
            lastBeat = beatService.findLastBeat()
        }

        Assertions.assertEquals(testBeat, lastBeat)
    }

    @Test
    fun `should throw an exception on finding last beat if there are no beats`() {
        this.emptyRepository()

        Assertions.assertThrows(NoBeatsReceivedException::class.java) {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should find first beat`() {
        // I think there should be more test beats to increase test quality.
        var firstBeat: Beat by Delegates.notNull()

        assertDoesNotThrow {
            firstBeat = beatService.findFirstBeat()
        }

        Assertions.assertEquals(testBeat, firstBeat)
    }

    @Test
    fun `should throw an exception on finding first beat if there are no beats`() {
        this.emptyRepository()

        Assertions.assertThrows(NoBeatsReceivedException::class.java) {
            beatService.findFirstBeat()
        }
    }

    @Test
    fun `should delete the beat`() {
        assertDoesNotThrow {
            beatService.deleteBeat(VALID_STUB)
        }

//        verify {
//            beatService.findBeatById(VALID_STUB)
//        }

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<BeatDeletedEvent>()
        )
    }

    private fun verifyBeatRegistrationPipelineCalls(deviceName: String, registerDevice: Boolean) {
        every {
            beatRepository.save(any())
        } returns buildBeat {
            beatTime = Instant.now()
            device = Device(deviceName)
        }

        var beat: Beat by Delegates.notNull()

        assertDoesNotThrow {
            beat = beatService.registerNewBeatWithDeviceIdentify(deviceName)
        }

        if (registerDevice) {
            verify {
                deviceService.registerNewDevice(deviceName)
            }
        }

        verify {
            deviceService.findDevice(byAll(deviceName))
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
            applicationEventTestListener.isLastEventWas<BeatReceivedEvent>()
        )

        Assertions.assertEquals(testBeat.id, beat.id)
        Assertions.assertEquals(Instant.now().epochSecond, beat.beatTime.epochSecond)
        Assertions.assertEquals(deviceName, beat.device.name)
        Assertions.assertEquals(activityService.getCurrentInactivityDuration(), beat.inactivityBeforeBeat)
    }

    private fun findAllByDeviceAndAssert(
        strategyType: DeviceFindingStrategyType
    ) {
        var all: List<Beat> by Delegates.notNull()
        val strategy = byStrategyType(VALID_STUB, strategyType)

        assertDoesNotThrow {
            all = beatService.findAllByDevice(strategy)
        }

        verify {
            beatService.findAllBeats()
        }

        Assertions.assertEquals(
            testBeats, all
        )
    }

    private fun emptyRepository() {
        every {
            beatRepository.findAll()
        } returns listOf()
    }
}