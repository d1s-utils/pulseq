/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

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
import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.exception.impl.DurationNotAvailableException
import uno.d1s.pulseq.exception.impl.NoBeatsReceivedException
import uno.d1s.pulseq.service.impl.IntervalServiceImpl
import uno.d1s.pulseq.testUtils.buildBeat
import uno.d1s.pulseq.util.time.betweenAbs
import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates

@SpringBootTest
@ContextConfiguration(classes = [IntervalServiceImpl::class])
internal class BeatIntervalServiceImplTest {

    @SpykBean
    private lateinit var activityService: IntervalServiceImpl

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
            beatService.findLast()
        } returns this.testBeats[2]

        every {
            beatService.findAll()
        } returns testBeats
    }

    @Test
    fun `should return valid current inactivity duration`() {
        var duration: Duration by Delegates.notNull()

        assertDoesNotThrow {
            duration = activityService.findCurrentAbsenceInterval()
        }

        verify {
            beatService.findLast()
        }

        Assertions.assertEquals(
            duration.toSeconds(), betweenAbs(
                beatService.findLast().instant, Instant.now()
            ).toSeconds()
        )
    }

    @Test
    fun `should return valid longest inactivity`() {
        var inactivity: BeatInterval by Delegates.notNull()

        assertDoesNotThrow {
            inactivity = activityService.getLongestInterval()
        }

        verify {
            activityService.findAllIntervals()
        }

        Assertions.assertEquals(IntervalType.INACTIVITY, inactivity.type)
        Assertions.assertEquals(testStartBeat, inactivity.start)
        Assertions.assertEquals(null, inactivity.end)
        Assertions.assertEquals(
            betweenAbs(testStartBeat.instant, Instant.now()).toSeconds(), inactivity.duration.toSeconds()
        )
    }

    @Test
    fun `should throw an exception when trying to get longest inactivity from empty beats data`() {
        every {
            beatService.findAll()
        } returns listOf()

        Assertions.assertThrows(DurationNotAvailableException::class.java) {
            activityService.getLongestInterval(processCurrent = false)
        }
    }

    @Test
    fun `should return valid prettier current inactivity`() {
        assertDoesNotThrow {
            activityService.getCurrentInactivityPretty()
        }

        verify {
            activityService.findCurrentAbsenceInterval()
        }

        // Duration#pretty() extension function
        // is tested in DurationExtTest.kt
    }

    @Test
    fun `should return valid current time span type`() {
        var intervalType: IntervalType by Delegates.notNull()

        assertDoesNotThrow {
            intervalType = activityService.findCurrentIntervalType()
        }

        verify {
            activityService.findCurrentInterval()
        }

        Assertions.assertEquals(IntervalType.INACTIVITY, intervalType)
    }

    @Test
    fun `should return valid current time span`() {
        var beatInterval: BeatInterval by Delegates.notNull()

        assertDoesNotThrow {
            beatInterval = activityService.findCurrentInterval()
        }

        Assertions.assertEquals(testStartBeat, beatInterval.start)
        Assertions.assertEquals(
            betweenAbs(testStartBeat.instant, Instant.now()).toSeconds(), beatInterval.duration.toSeconds()
        )
        Assertions.assertEquals(IntervalType.INACTIVITY, beatInterval.type)
        Assertions.assertEquals(null, beatInterval.end)

        verify {
            beatService.findLast()
        }
    }

    @Test
    fun `should throw an exception on getting current time span`() {
        every {
            beatService.findLast()
        } throws NoBeatsReceivedException

        Assertions.assertThrows(DurationNotAvailableException::class.java) {
            activityService.findCurrentInterval()
        }
    }

    @Test
    fun `should return valid time spans`() {
        var allBeatIntervals: List<BeatInterval> by Delegates.notNull()

        assertDoesNotThrow {
            // not including current time span. This method is being
            // tested at `should return valid current time span`()
            // and `should throw an exception on getting current time span`()
            allBeatIntervals = activityService.findAllIntervals(false)
        }

        Assertions.assertEquals(
            listOf(
                BeatInterval(
                    testInactivities[1],
                    IntervalType.ACTIVITY,
                    testBeats[0],
                    testBeats[2]
                ), BeatInterval(
                    testInactivities[2],
                    IntervalType.INACTIVITY,
                    testBeats[2],
                    testBeats[3]
                )
            ), allBeatIntervals
        )
    }

    @Test
    fun `should return valid last registered time span`() {
        var lastRegisteredBeatInterval: BeatInterval by Delegates.notNull()

        assertDoesNotThrow {
            lastRegisteredBeatInterval = activityService.getLastRegisteredDuration()
        }

        verify {
            activityService.findAllIntervals(false)
        }

        Assertions.assertEquals(activityService.findAllIntervals(false).last(), lastRegisteredBeatInterval)
    }
}