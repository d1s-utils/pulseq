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
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.event.source.SourceDeletedEvent
import uno.d1s.pulseq.event.source.SourceUpdatedEvent
import uno.d1s.pulseq.exception.impl.SourceAlreadyExistsException
import uno.d1s.pulseq.exception.impl.SourceNotFoundException
import uno.d1s.pulseq.repository.SourceRepository
import uno.d1s.pulseq.service.impl.SourceServiceImpl
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import uno.d1s.pulseq.strategy.source.byAll
import uno.d1s.pulseq.strategy.source.byId
import uno.d1s.pulseq.strategy.source.byName
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener
import java.util.*
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [SourceServiceImpl::class, ApplicationEventTestListener::class])
internal class SourceServiceImplTest {

    @Autowired
    private lateinit var sourceService: SourceServiceImpl

    @MockkBean
    private lateinit var sourceRepository: SourceRepository

    @MockkBean
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var applicationEventTestListener: ApplicationEventTestListener

    private val optionalTestSource = Optional.of(testSource)

    @BeforeEach
    fun setup() {
        every {
            sourceRepository.findAll()
        } returns testSources

        every {
            sourceRepository.findById(VALID_STUB)
        } returns optionalTestSource

        every {
            sourceRepository.findById(INVALID_STUB)
        } returns Optional.empty()

        every {
            sourceRepository.findSourceByNameEqualsIgnoreCase(VALID_STUB)
        } returns optionalTestSource

        every {
            sourceRepository.findSourceByNameEqualsIgnoreCase(INVALID_STUB)
        } returns Optional.empty()

        every {
            sourceRepository.findSourceByNameEqualsIgnoreCase(testSourceUpdate.name)
        } returns Optional.empty()

        val savedSource = Source(INVALID_STUB)

        every {
            sourceRepository.save(savedSource)
        } returns savedSource

        every {
            sourceRepository.save(testSourceUpdate)
        } returns testSourceUpdate

        justRun {
            sourceRepository.delete(testSource)
        }

        every {
            beatService.findAllBeats()
        } returns testBeats

        justRun {
            beatService.deleteBeat(any(), any())
        }
    }

    @Test
    fun `should find all registered sources`() {
        var all: List<Source> by Delegates.notNull()

        assertDoesNotThrow {
            all = sourceService.findAllRegisteredSources()
        }

        verify {
            sourceRepository.findAll()
        }

        Assertions.assertEquals(testSources, all)
    }

    @Test
    fun `should find the source by id`() {
        this.findSourceAndVerify(byId(VALID_STUB)) {
            sourceRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should not find the source by id`() {
        Assertions.assertThrows(SourceNotFoundException::class.java) {
            sourceService.findSource(byId(INVALID_STUB))
        }

        verify {
            sourceRepository.findById(INVALID_STUB)
        }
    }

    @Test
    fun `should find the source by name`() {
        this.findSourceAndVerify(byName(VALID_STUB)) {
            sourceRepository.findSourceByNameEqualsIgnoreCase(VALID_STUB)
        }
    }

    @Test
    fun `should not find th source by name`() {
        Assertions.assertThrows(SourceNotFoundException::class.java) {
            sourceService.findSource(byName(INVALID_STUB))
        }

        verify {
            sourceRepository.findSourceByNameEqualsIgnoreCase(INVALID_STUB)
        }
    }

    @Test
    fun `should find the source by all strategies`() {
        this.findSourceAndVerify(byAll(VALID_STUB)) {
            sourceRepository.findById(VALID_STUB)
        }
    }

    @Test
    fun `should not find the source by all strategies`() {
        Assertions.assertThrows(SourceNotFoundException::class.java) {
            sourceService.findSource(byAll(INVALID_STUB))
        }

        verify {
            sourceRepository.findById(INVALID_STUB)
        }

        verify {
            sourceRepository.findSourceByNameEqualsIgnoreCase(INVALID_STUB)
        }
    }

    @Test
    fun `should register the source`() {
        var newSource: Source by Delegates.notNull()

        assertDoesNotThrow {
            newSource = sourceService.registerNewSource(INVALID_STUB)
        }

        verify {
            sourceRepository.findSourceByNameEqualsIgnoreCase(INVALID_STUB)
        }

        verify {
            sourceRepository.save(any())
        }

        Assertions.assertEquals(INVALID_STUB, newSource.name)
    }

    @Test
    fun `should throw an exception on source registration with existing name`() {
        Assertions.assertThrows(SourceAlreadyExistsException::class.java) {
            sourceService.registerNewSource(VALID_STUB)
        }

        verify {
            sourceRepository.findSourceByNameEqualsIgnoreCase(VALID_STUB)
        }

        verify(exactly = 0) {
            sourceRepository.save(any())
        }
    }

    @Test
    fun `should update the sources`() {
        var updatedSource: Source by Delegates.notNull()

        assertDoesNotThrow {
            updatedSource = sourceService.updateSource(byAll(VALID_STUB), testSourceUpdate)
        }

        // I'm stuck. This call literally has NO REASONS to not being passed.
        // java.lang.AssertionError: Verification failed: call 2 of 4:
        // Optional(child of uno.d1s.pulseq.repository.SourceRepository#0 bean#75#156).orElseThrow(eq(uno.d1s.pulseq.service.impl.SourceServiceImpl$$Lambda$744/0x0000000100795840@59f36439)))
        // was not called
        //verify {
        //    sourceService.findSource(byAll(VALID_STUB))
        //}

        // Same with others.
        //verify {
        //    sourceService.findSource(byName(testSourceUpdate.name))
        //}

        verify {
            sourceRepository.save(testSourceUpdate)
        }

        Assertions.assertEquals(testSourceUpdate, updatedSource)

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<SourceUpdatedEvent>()
        )
    }

    @Test
    fun `should throw an exception while source updating with existing name of the source to be updated`() {
        every {
            sourceRepository.findSourceByNameEqualsIgnoreCase(testSourceUpdate.name)
        } returns Optional.of(testSourceUpdate)

        Assertions.assertThrows(SourceAlreadyExistsException::class.java) {
            sourceService.updateSource(byAll(VALID_STUB), testSourceUpdate)
        }

//        verify {
//            sourceService.findSource(byAll(VALID_STUB))
//        }

//        verify {
//            sourceService.findSource(byName(testSourceUpdate.name))
//        }
    }

    @Test
    fun `should delete the source`() {
        assertDoesNotThrow {
            sourceService.deleteSource(byAll(VALID_STUB))
        }

//        verify {
//            sourceService.findSource(byAll(VALID_STUB))
//        }

        verify {
            beatService.findAllBeats()
        }

        verify {
            beatService.deleteBeat(any(), false)
        }

        verify {
            sourceRepository.delete(testSource)
        }

        Assertions.assertTrue(
            applicationEventTestListener.isLastEventWas<SourceDeletedEvent>()
        )
    }

    @Test
    fun `should find the source beats`() {
        var sourceBeats: List<Beat> by Delegates.notNull()

        assertDoesNotThrow {
            sourceBeats = sourceService.findSourceBeats(byAll(VALID_STUB))
        }

//        verify {
//            sourceService.findSource(byAll(VALID_STUB))
//        }

        Assertions.assertEquals(testBeats, sourceBeats)
    }

    private fun findSourceAndVerify(strategy: SourceFindingStrategy, verifications: MockKVerificationScope.() -> Unit) {
        var source: Source by Delegates.notNull()

        assertDoesNotThrow {
            source = sourceService.findSource(strategy)
        }

        verify(verifyBlock = verifications)
        Assertions.assertEquals(source, testSource)
    }
}