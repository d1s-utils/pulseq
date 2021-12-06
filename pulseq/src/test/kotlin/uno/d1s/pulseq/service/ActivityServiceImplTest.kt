package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.exception.impl.TimeSpansNotAvailableException
import uno.d1s.pulseq.service.impl.ActivityServiceImpl
import uno.d1s.pulseq.util.buildBeat
import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [ActivityServiceImpl::class])
internal class ActivityServiceImplTest {

    @SpykBean
    private lateinit var activityService: ActivityServiceImpl

    @MockkBean
    private lateinit var beatService: BeatService

    @MockkBean
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

    private val testInactivities = listOf<Duration>(
        Duration.ofMinutes(5), Duration.ofMinutes(10), Duration.ofMinutes(15)
    )

    private val testInstants = testInactivities.map {
        Instant.ofEpochMilli(it.toMillis())
    }

    private val testBeats = mutableListOf(buildBeat {
        inactivityBeforeBeat = null
    }).apply {
        addAll(testInstants.zip(testInactivities).map {
            buildBeat {
                beatTime = it.first
                inactivityBeforeBeat = it.second
            }
        })
    }

    private val testStartBeat = testBeats[2]

    @BeforeEach
    fun setup() {
        every {
            activityConfigurationProperties.activityDelimiter
        } returns Duration.ofMinutes(15)

        every {
            activityConfigurationProperties.long
        } returns Duration.ofMinutes(30)

        every {
            activityConfigurationProperties.warning
        } returns Duration.ofMinutes(50)

        every {
            beatService.findLastBeat()
        } returns this.testBeats[2]

        every {
            beatService.findAllBeats()
        } returns testBeats
    }

    @Test
    fun `should return valid current inactivity duration`() {
        var duration: Duration by Delegates.notNull()

        assertDoesNotThrow {
            duration = activityService.getCurrentInactivityDuration()
        }

        verify {
            beatService.findLastBeat()
        }

        Assertions.assertEquals(
            duration.toSeconds(), Duration.between(
                beatService.findLastBeat().beatTime, Instant.now()
            ).abs().toSeconds()
        )
    }

    @Test
    fun `should return valid longest inactivity`() {
        var inactivity: TimeSpan by Delegates.notNull()

        assertDoesNotThrow {
            inactivity = activityService.getLongestInactivity()
        }

        verify {
            activityService.getAllTimeSpans()
        }

        Assertions.assertEquals(TimeSpanType.INACTIVITY, inactivity.type)
        Assertions.assertEquals(testStartBeat, inactivity.startBeat)
        Assertions.assertEquals(null, inactivity.endBeat)
        Assertions.assertEquals(
            Duration.between(testStartBeat.beatTime, Instant.now()).toSeconds(), inactivity.duration.toSeconds()
        )
    }

    @Test
    fun `should throw an exception when trying to get longest inactivity from empty beats data`() {
        every {
            beatService.findAllBeats()
        } returns listOf()

        Assertions.assertThrows(TimeSpansNotAvailableException::class.java) {
            activityService.getLongestInactivity(processCurrent = false)
        }
    }

    @Test
    fun `should return valid prettier current inactivity`() {
        assertDoesNotThrow {
            activityService.getCurrentInactivityPretty()
        }

        verify {
            activityService.getCurrentInactivityDuration()
        }

        // Duration#pretty() extension function
        // is tested in uno.d1s.pulseq.util.DurationExtTest.kt
    }

    @Test
    fun `should return valid current inactivity relevance level`() {
        var level: InactivityRelevanceLevel by Delegates.notNull()

        assertDoesNotThrow {
            level = activityService.getCurrentInactivityRelevanceLevel()
        }

        verify {
            activityService.getCurrentInactivityDuration()
        }

        Assertions.assertEquals(InactivityRelevanceLevel.WARNING, level)
    }

    @Test
    fun `should return valid decision if inactivity relevance is common or not`() {
        var decision: Boolean by Delegates.notNull()

        assertDoesNotThrow {
            decision = activityService.isInactivityRelevanceLevelNotCommon()
        }

        verify {
            activityService.getCurrentInactivityRelevanceLevel()
        }

        Assertions.assertTrue(decision)
    }

    @Test
    fun `should return valid current time span type`() {
        var timeSpanType: TimeSpanType by Delegates.notNull()

        assertDoesNotThrow {
            timeSpanType = activityService.getCurrentTimeSpanType()
        }

        verify {
            activityService.getCurrentTimeSpan()
        }

        Assertions.assertEquals(TimeSpanType.INACTIVITY, timeSpanType)
    }

    @Test
    fun `should return valid current time span`() {
        var timeSpan: TimeSpan by Delegates.notNull()

        assertDoesNotThrow {
            timeSpan = activityService.getCurrentTimeSpan()
        }

        Assertions.assertEquals(testStartBeat, timeSpan.startBeat)
        Assertions.assertEquals(
            Duration.between(testStartBeat.beatTime, Instant.now()).toSeconds(), timeSpan.duration.toSeconds()
        )
        Assertions.assertEquals(TimeSpanType.INACTIVITY, timeSpan.type)
        Assertions.assertEquals(null, timeSpan.endBeat)

        verify {
            activityService.getCurrentInactivityRelevanceLevel()
        }

        verify {
            beatService.findLastBeat()
        }
    }

    @Test
    fun `should throw an exception on getting current time span`() {
        every {
            beatService.findLastBeat()
        } throws NoBeatsReceivedException

        Assertions.assertThrows(TimeSpansNotAvailableException::class.java) {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return valid time spans`() {
        var allTimeSpans: List<TimeSpan> by Delegates.notNull()

        assertDoesNotThrow {
            // not including current time span. This method is being
            // tested at `should return valid current time span`()
            // and `should throw an exception on getting current time span`()
            allTimeSpans = activityService.getAllTimeSpans(false)
        }

        Assertions.assertEquals(
            listOf(
                TimeSpan(
                    testInactivities[1],
                    TimeSpanType.ACTIVITY,
                    InactivityRelevanceLevel.COMMON,
                    testBeats[0],
                    testBeats[2]
                ), TimeSpan(
                    testInactivities[2],
                    TimeSpanType.INACTIVITY,
                    InactivityRelevanceLevel.COMMON,
                    testBeats[2],
                    testBeats[3]
                )
            ), allTimeSpans
        )
    }

    @Test
    fun `should return valid last registered time span`() {
        var lastRegisteredTimeSpan: TimeSpan by Delegates.notNull()

        assertDoesNotThrow {
            lastRegisteredTimeSpan = activityService.getLastRegisteredTimeSpan()
        }

        verify {
            activityService.getAllTimeSpans(false)
        }

        Assertions.assertEquals(activityService.getAllTimeSpans(false).last(), lastRegisteredTimeSpan)
    }
}