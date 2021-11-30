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
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.exception.TimeSpansNotAvailableException
import uno.d1s.pulseq.service.impl.ActivityServiceImpl
import uno.d1s.pulseq.util.buildBeat
import uno.d1s.pulseq.util.testBeat
import java.time.Duration
import java.time.Instant

@SpringBootTest
@ContextConfiguration(classes = [ActivityServiceImpl::class])
internal class ActivityServiceImplTest {

    @Autowired
    private lateinit var activityService: ActivityServiceImpl

    @MockkBean
    private lateinit var beatService: BeatService

    @MockkBean
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            activityConfigurationProperties.activityDelimiter
        } returns Duration.ofMinutes(15)

        every {
            activityConfigurationProperties.common
        } returns Duration.ofMinutes(30)

        every {
            activityConfigurationProperties.warning
        } returns Duration.ofMinutes(50)

        val inactivity = listOf<Duration>(
            Duration.ofMinutes(10), Duration.ofMinutes(16), Duration.ofMinutes(5)
        )

        val instants = inactivity.map {
            Instant.ofEpochMilli(it.toMillis())
        }

        every {
            beatService.findAllBeats()
        } returns mutableListOf(buildBeat {
            inactivityBeforeBeat = null
        }).apply {
            addAll(instants.zip(inactivity).map {
                buildBeat {
                    beatTime = it.first
                    inactivityBeforeBeat = it.second
                }
            })
        }

        every {
            beatService.findLastBeat()
        } returns testBeat
    }

    @Test
    fun `should return valid current inactivity`() {
        activityService.getCurrentInactivityDuration()

        verify {
            beatService.findLastBeat()
        }
    }

    // the tests below only needs a check for exceptions
    // please, if you see this way insufficient, customize this tests
    @Test
    fun `should return valid longest inactivity`() {
        assertDoesNotThrow {
            activityService.getLongestInactivity()
        }
    }

    @Test
    fun `should return valid prettier current inactivity`() {
        assertDoesNotThrow {
            activityService.getCurrentInactivityPretty()
        }
    }

    @Test
    fun `should return valid current inactivity relevance level`() {
        assertDoesNotThrow {
            activityService.getCurrentInactivityRelevanceLevel()
        }
    }

    @Test
    fun `should return valid decision if inactivity relevance is common or not`() {
        assertDoesNotThrow {
            activityService.isInactivityRelevanceLevelNotCommon()
        }
    }

    @Test
    fun `should return valid current time span type`() {
        assertDoesNotThrow {
            activityService.getCurrentTimeSpanType()
        }
    }

    @Test
    fun `should return valid current time span`() {
        assertDoesNotThrow {
            activityService.getCurrentTimeSpan()
        }

        verify {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should thrown an exception on getting current time span`() {
        every {
            beatService.findLastBeat()
        } throws NoBeatsReceivedException

        Assertions.assertThrows(TimeSpansNotAvailableException::class.java) {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return valid time spans`() {
        assertDoesNotThrow {
            activityService.getAllTimeSpans()
        }

        val all = activityService.getAllTimeSpans()
        Assertions.assertTrue(all.isNotEmpty())
    }

    @Test
    fun `should return valid last registered time span`() {
        assertDoesNotThrow {
            activityService.getLastRegisteredTimeSpan()
        }
    }
}