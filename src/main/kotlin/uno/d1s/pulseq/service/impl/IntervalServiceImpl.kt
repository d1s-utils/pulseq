/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.domain.activity.impl.InstantInterval
import uno.d1s.pulseq.exception.impl.DurationNotAvailableException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.IntervalService
import uno.d1s.pulseq.strategy.DomainFindingStrategy
import uno.d1s.pulseq.util.forEachPartition
import uno.d1s.pulseq.util.nextAfterOrNull
import uno.d1s.pulseq.util.previousBeforeOrNull
import uno.d1s.pulseq.util.time.betweenAbs
import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates

@Service
class IntervalServiceImpl : IntervalService {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

    override fun findCurrentAbsenceInterval(strategy: DomainFindingStrategy): InstantInterval =
        InstantInterval.from(beatService.findLast().instant, Instant.now())

    override fun findLongestInterval(strategy: DomainFindingStrategy, type: IntervalType?): BeatInterval =
        this.findAllIntervals(strategy, type).let { all ->
            all.firstOrNull {
                it.duration == (all.maxOfOrNull { interval ->
                    interval.duration
                } ?: throw DurationNotAvailableException())
            } ?: throw DurationNotAvailableException()
        }

    override fun findCurrentIntervalType(strategy: DomainFindingStrategy): IntervalType =
        this.findCurrentInterval(strategy).type

    override fun findCurrentInterval(strategy: DomainFindingStrategy): BeatInterval = runCatching {
        beatService.findLast().let { lastBeat ->
            val duration = betweenAbs(lastBeat.instant, Instant.now())

            BeatInterval(
                duration, getTypeByDuration(duration), lastBeat
            )
        }
    }.getOrElse {
        throw DurationNotAvailableException()
    }

    override fun findAllIntervals(strategy: DomainFindingStrategy, type: IntervalType?): List<BeatInterval> =
        mutableListOf<BeatInterval>().apply {
            var activityHandling = false
            var firstActivityBeatConsumed = false
            var postActivityHandling = false
            var firstActivityBeat: Beat by Delegates.notNull()
            val allBeats = beatService.findAll().sortedBy {
                it.instant
            }

            if (allBeats.size > 1) {
                allBeats.forEachPartition { partition ->
                    val nextBeat = allBeats.nextAfterOrNull(this) ?: this

                    if (postActivityHandling) {
                        val duration = this.inactivityBeforeBeat!!

                        add(
                            BeatInterval(
                                duration, IntervalType.INACTIVITY, allBeats.previousBeforeOrNull(this)!!, this
                            )
                        )

                        postActivityHandling = false
                    }

                    if (nextBeat.isActivityDelimiterExceeded() && !activityHandling) {
                        if (this != nextBeat) {
                            add(
                                BeatInterval(
                                    nextBeat.inactivityBeforeBeat!!, IntervalType.INACTIVITY, this, nextBeat
                                )
                            )
                        }

                        return@forEachPartition (allBeats.indexOf(nextBeat) + 1)..allBeats.size
                    } else {
                        activityHandling = true

                        if (!firstActivityBeatConsumed) {
                            firstActivityBeat = this
                            firstActivityBeatConsumed = true
                        }

                        // The duration is being calculated between two beats,
                        // but if collection contains only one beat, startBeat will be lastBeat
                        if (nextBeat.isActivityDelimiterExceeded() || this == partition.last()) {
                            add(
                                BeatInterval(
                                    betweenAbs(firstActivityBeat.instant, this.instant),
                                    IntervalType.ACTIVITY,
                                    firstActivityBeat,
                                    this
                                )
                            )
                            activityHandling = false
                            firstActivityBeatConsumed = false
                            postActivityHandling = true
                        }

                        return@forEachPartition allBeats.indexOf(this) + 1..allBeats.size
                    }
                }
            }

            val current = this@IntervalServiceImpl.findCurrentInterval(strategy)

            runCatching {
                if (this@IntervalServiceImpl.getLastRegisteredDuration(strategy).type == IntervalType.INACTIVITY || current.duration.isActivityDelimiterExceeded()) {
                    add(current)
                }
            }.getOrElse {
                add(current)
            }
        }

    private fun getLastRegisteredDuration(strategy: DomainFindingStrategy): BeatInterval =
        this.findAllIntervals(strategy, false).last()

    private fun getTypeByDuration(duration: Duration): IntervalType =
        if (duration >= activityConfigurationProperties.activityDelimiter) {
            IntervalType.INACTIVITY
        } else {
            IntervalType.ACTIVITY
        }

    private fun Beat.isActivityDelimiterExceeded(): Boolean {
        return (this.inactivityBeforeBeat ?: return false).isActivityDelimiterExceeded()
    }

    private fun Duration.isActivityDelimiterExceeded() = this >= activityConfigurationProperties.activityDelimiter
}