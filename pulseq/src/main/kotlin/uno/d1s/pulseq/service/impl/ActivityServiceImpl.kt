package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.impl.TimeSpansNotAvailableException
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.util.forEachPartition
import uno.d1s.pulseq.util.nextAfterOrNull
import uno.d1s.pulseq.util.pretty
import uno.d1s.pulseq.util.previousBeforeOrNull
import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates

@Service
class ActivityServiceImpl : ActivityService {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

    override fun getCurrentInactivityDuration(): Duration = Duration.between(
        beatService.findLastBeat().beatTime, Instant.now()
    ).abs()

    override fun getLongestTimeSpan(excludeActivity: Boolean, processCurrent: Boolean): TimeSpan =
        this.getAllTimeSpans(processCurrent).let { all ->
            all.firstOrNull {
                (excludeActivity && it.type == TimeSpanType.INACTIVITY) && it.duration == (all.maxOfOrNull { timeSpan ->
                    timeSpan.duration
                } ?: throw TimeSpansNotAvailableException())
            } ?: throw TimeSpansNotAvailableException()
        }

    override fun getCurrentInactivityPretty(): String = this.getCurrentInactivityDuration().pretty()

    override fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel =
        this.getCurrentInactivityDuration().getInactivityRelevanceLevel()

    override fun isInactivityRelevanceLevelNotCommon(): Boolean =
        this.getCurrentInactivityRelevanceLevel() != InactivityRelevanceLevel.COMMON

    override fun getCurrentTimeSpanType(): TimeSpanType = this.getCurrentTimeSpan().type

    override fun getCurrentTimeSpan(): TimeSpan = runCatching {
        beatService.findLastBeat().let { lastBeat ->
            val duration = Duration.between(lastBeat.beatTime, Instant.now()).abs()

            TimeSpan(
                duration, getTypeByDuration(duration), this.getCurrentInactivityRelevanceLevel(), lastBeat
            )
        }
    }.getOrElse {
        throw TimeSpansNotAvailableException()
    }

    override fun getAllTimeSpans(includeCurrent: Boolean): List<TimeSpan> = mutableListOf<TimeSpan>().apply {
        var activityHandling = false
        var firstActivityBeatConsumed = false
        var postActivityHandling = false
        var firstActivityBeat: Beat by Delegates.notNull()
        val allBeats = beatService.findAllBeats().sortedBy {
            it.beatTime
        }

        if (allBeats.size > 1) {
            allBeats.forEachPartition { partition ->
                val nextBeat = allBeats.nextAfterOrNull(this) ?: this

                if (postActivityHandling) {
                    val duration = this.inactivityBeforeBeat!!

                    add(
                        TimeSpan(
                            duration,
                            TimeSpanType.INACTIVITY,
                            duration.getInactivityRelevanceLevel(),
                            allBeats.previousBeforeOrNull(this)!!,
                            this
                        )
                    )

                    postActivityHandling = false
                }

                if (nextBeat.isActivityDelimiterExceeded() && !activityHandling) {
                    if (this != nextBeat) {
                        val duration = nextBeat.inactivityBeforeBeat!!

                        add(
                            TimeSpan(
                                duration,
                                TimeSpanType.INACTIVITY,
                                duration.getInactivityRelevanceLevel(),
                                this,
                                nextBeat
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
                        val duration = Duration.between(firstActivityBeat.beatTime, this.beatTime).abs()

                        add(
                            TimeSpan(
                                duration,
                                TimeSpanType.ACTIVITY,
                                duration.getInactivityRelevanceLevel(),
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

        if (includeCurrent) {
            val current = this@ActivityServiceImpl.getCurrentTimeSpan()

            runCatching {
                if (this@ActivityServiceImpl.getLastRegisteredTimeSpan().type == TimeSpanType.INACTIVITY || current.duration.isActivityDelimiterExceeded()) {
                    add(current)
                }
            }.getOrElse {
                add(current)
            }
        }
    }

    override fun getLastRegisteredTimeSpan(): TimeSpan = this.getAllTimeSpans(false).last()

    private fun Duration.getInactivityRelevanceLevel() = if (this >= activityConfigurationProperties.warning) {
        InactivityRelevanceLevel.WARNING
    } else if (this >= activityConfigurationProperties.long) {
        InactivityRelevanceLevel.LONG
    } else {
        InactivityRelevanceLevel.COMMON
    }

    private fun getTypeByDuration(duration: Duration): TimeSpanType =
        if (duration >= activityConfigurationProperties.activityDelimiter) {
            TimeSpanType.INACTIVITY
        } else {
            TimeSpanType.ACTIVITY
        }

    private fun Beat.isActivityDelimiterExceeded(): Boolean {
        return (this.inactivityBeforeBeat ?: return false).isActivityDelimiterExceeded()
    }

    private fun Duration.isActivityDelimiterExceeded() = this >= activityConfigurationProperties.activityDelimiter
}