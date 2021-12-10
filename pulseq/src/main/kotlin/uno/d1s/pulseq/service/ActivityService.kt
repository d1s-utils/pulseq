package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import java.time.Duration

interface ActivityService {

    fun getCurrentInactivityDuration(): Duration

    fun getLongestTimeSpan(excludeActivity: Boolean = true, processCurrent: Boolean = true): TimeSpan

    fun getCurrentInactivityPretty(): String

    fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel

    fun isInactivityRelevanceLevelNotCommon(): Boolean

    fun getCurrentTimeSpanType(): TimeSpanType

    fun getCurrentTimeSpan(): TimeSpan

    fun getAllTimeSpans(includeCurrent: Boolean = true): List<TimeSpan>

    fun getLastRegisteredTimeSpan(): TimeSpan
}