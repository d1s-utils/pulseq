/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import java.time.Duration

interface ActivityService {

    fun getCurrentInactivityDuration(): Duration

    fun getLongestTimeSpan(type: TimeSpanType? = null, processCurrent: Boolean = true): TimeSpan

    fun getCurrentInactivityPretty(): String

    fun getCurrentTimeSpanType(): TimeSpanType

    fun getCurrentTimeSpan(): TimeSpan

    fun getAllTimeSpans(includeCurrent: Boolean = true): List<TimeSpan>

    fun getLastRegisteredTimeSpan(): TimeSpan
}