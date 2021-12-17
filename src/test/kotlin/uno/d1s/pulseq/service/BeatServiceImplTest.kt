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
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.event.beat.BeatDeletedEvent
import uno.d1s.pulseq.event.beat.BeatReceivedEvent
import uno.d1s.pulseq.exception.impl.BeatNotFoundException
import uno.d1s.pulseq.exception.impl.SourceNotFoundException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.impl.BeatServiceImpl
import uno.d1s.pulseq.strategy.source.*
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
    private lateinit var sourceService: SourceService

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
            sourceService.registerNewSource(VALID_STUB)
        } returns testSource

        every {
            sourceService.registerNewSource(INVALID_STUB)
        } returns Source(INVALID_STUB)

        every {
            sourceService.findSource(byAll(VALID_STUB))
        } returns testSource

        every {
            sourceService.findSource(byAll(INVALID_STUB))
        } throws SourceNotFoundException()

        every {
            sourceService.findSource(byId(VALID_STUB))
        } returns testSource

        every {
            sourceService.findSource(byName(VALID_STUB))
        } returns testSource

        every {
            sourceService.findSource(byName(INVALID_STUB))
        } throws SourceNotFoundException() andThen Source(INVALID_STUB)

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
    fun `should register the beat with non existent source`() {
        this.verifyBeatRegistrationPipelineCalls(INVALID_STUB, true)
    }

    @Test
    fun `should register the beat with existing source`() {
        this.verifyBeatRegistrationPipelineCalls(VALID_STUB, false)
    }

    @Test
    fun `should find all beats by source id`() {
        this.findAllBySourceAndAssert(SourceFindingStrategyType.BY_ID)
    }

    @Test
    fun `should find all beats by source name`() {
        this.findAllBySourceAndAssert(SourceFindingStrategyType.BY_NAME)
    }

    @Test
    fun `should find all beats by source identify`() {
        this.findAllBySourceAndAssert(SourceFindingStrategyType.BY_ALL)
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
    fun `should find total beats numbers by sources`() {
        var beatsBySources: Map<String, Int> by Delegates.notNull()

        assertDoesNotThrow {
            beatsBySources = beatService.totalBeatsBySources()
        }

        Assertions.assertEquals(
            mapOf(
                VALID_STUB to 1
            ),
            beatsBySources
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

    private fun verifyBeatRegistrationPipelineCalls(sourceName: String, registerSource: Boolean) {
        every {
            beatRepository.save(any())
        } returns buildBeat {
            beatTime = Instant.now()
            source = Source(sourceName)
        }

        var beat: Beat by Delegates.notNull()

        assertDoesNotThrow {
            beat = beatService.registerNewBeatWithSourceIdentify(sourceName)
        }

        if (registerSource) {
            verify {
                sourceService.registerNewSource(sourceName)
            }
        }

        verify {
            sourceService.findSource(byAll(sourceName))
        }

        verify {
            activityService.getCurrentInactivityDuration()
        }

        verify {
            beatRepository.save(any())
        }

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<BeatReceivedEvent>()
        )

        Assertions.assertEquals(testBeat.id, beat.id)
        Assertions.assertEquals(Instant.now().epochSecond, beat.beatTime.epochSecond)
        Assertions.assertEquals(sourceName, beat.source.name)
        Assertions.assertEquals(activityService.getCurrentInactivityDuration(), beat.inactivityBeforeBeat)
    }

    private fun findAllBySourceAndAssert(
        strategyType: SourceFindingStrategyType
    ) {
        var all: List<Beat> by Delegates.notNull()
        val strategy = byStrategyType(VALID_STUB, strategyType)

        assertDoesNotThrow {
            all = beatService.findAllBySource(strategy)
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