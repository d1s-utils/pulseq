package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.ActivityConfigurationProperties
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.exception.TimeSpansNotAvailableException
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.util.forEachPartition
import uno.d1s.pulseq.util.nextAfterOrNull
import uno.d1s.pulseq.util.pretty
import uno.d1s.pulseq.util.previousBeforeOrNull
import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates

@Service("inactivityStatusService")
class ActivityServiceImpl : ActivityService {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var activityConfigurationProperties: ActivityConfigurationProperties

    override fun getCurrentInactivityDuration(): Duration = Instant.now().let { now ->
        Duration.between(
            beatService.findLastBeat().beatTime, now
        ).abs()
    }

    override fun getLongestInactivity(): TimeSpan =
        this.getAllTimeSpans().let { all -> // Take a look into this usage when using @Cacheable!
            all.firstOrNull {
                it.type == TimeSpanType.INACTIVITY
                        && it.duration == (all.maxOfOrNull { timeSpan ->
                    timeSpan.duration
                } ?: throw TimeSpansNotAvailableException())
            }?.let {
                val current = this.getCurrentTimeSpan()

                if (current.duration > it.duration) {
                    current
                } else {
                    it
                }
            } ?: throw TimeSpansNotAvailableException()
        }

    override fun getCurrentInactivityPretty(): String = this.getCurrentInactivityDuration().pretty()

    override fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel =
        if (this.isLongInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.LONG
        } else if (this.isWarningInactivityDurationPointExceeded()) {
            InactivityRelevanceLevel.WARNING
        } else {
            InactivityRelevanceLevel.COMMON
        }

    override fun isInactivityRelevanceLevelNotCommon(): Boolean =
        this.getCurrentInactivityRelevanceLevel() != InactivityRelevanceLevel.COMMON

    override fun getCurrentTimeSpanType(): TimeSpanType = this.getCurrentTimeSpan().type

    override fun getCurrentTimeSpan(): TimeSpan = runCatching {
        beatService.findLastBeat().let { lastBeat ->
            val duration = Duration.between(lastBeat.beatTime, Instant.now()).abs()

            TimeSpan(
                duration, getTypeByDuration(duration), lastBeat
            )
        }
    }.getOrElse {
        throw TimeSpansNotAvailableException()
    }

    override fun getAllTimeSpans(includeCurrent: Boolean): List<TimeSpan> =
        mutableListOf<TimeSpan>().apply {
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
                        add(
                            TimeSpan(
                                this.inactivityBeforeBeat!!,
                                TimeSpanType.INACTIVITY,
                                allBeats.previousBeforeOrNull(this)!!,
                                this
                            )
                        )

                        postActivityHandling = false
                    }

                    if (nextBeat.isActivityDelimiterExceeded() && !activityHandling) {
                        if (this != nextBeat) {
                            add(
                                TimeSpan(
                                    nextBeat.inactivityBeforeBeat!!,
                                    TimeSpanType.INACTIVITY,
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

                        if (nextBeat.isActivityDelimiterExceeded() || this == partition.last()) {
                            // The duration is being calculated between two beats,
                            // but if collection contains only one beat, startBeat will be lastBeat
                            add(
                                TimeSpan(
                                    Duration.between(firstActivityBeat.beatTime, this.beatTime).abs(),
                                    TimeSpanType.ACTIVITY,
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

            val current = this@ActivityServiceImpl.getCurrentTimeSpan()

            runCatching {
                if ((includeCurrent && this@ActivityServiceImpl.getLastRegisteredTimeSpan().type == TimeSpanType.INACTIVITY)
                    || current.duration.isActivityDelimiterExceeded()
                ) {
                    add(current)
                }
            }.getOrElse {
                add(current)
            }
        }

    override fun getLastRegisteredTimeSpan(): TimeSpan =
        this.getAllTimeSpans(false).last()

    private fun isLongInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(activityConfigurationProperties.common)

    private fun isWarningInactivityDurationPointExceeded() =
        this.isDurationPointExceeded(activityConfigurationProperties.warning)

    private fun isDurationPointExceeded(duration: Duration) = try {
        this.getCurrentInactivityDuration() > duration
    } catch (_: NoBeatsReceivedException) {
        false
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

    private fun Duration.isActivityDelimiterExceeded() =
        this >= activityConfigurationProperties.activityDelimiter
}