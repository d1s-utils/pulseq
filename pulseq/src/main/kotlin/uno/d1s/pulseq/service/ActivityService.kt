package uno.d1s.pulseq.service

import uno.d1s.pulseq.event.inactivity.InactivityRelevanceLevel
import java.time.Duration

interface ActivityService {

    fun getCurrentInactivity(): Duration

    fun getLongestInactivity(): Duration

    fun getCurrentInactivityPretty(): String

    fun getCurrentInactivityRelevanceLevel(): InactivityRelevanceLevel

    fun isInactivityRelevanceLevelNotCommon(): Boolean
}