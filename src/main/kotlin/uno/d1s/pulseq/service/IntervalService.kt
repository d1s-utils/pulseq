/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.domain.activity.impl.InstantInterval
import uno.d1s.pulseq.strategy.DomainFindingStrategy

interface IntervalService {

    fun findCurrentAbsenceInterval(strategy: DomainFindingStrategy): InstantInterval

    fun findLongestInterval(
        strategy: DomainFindingStrategy,
        type: IntervalType? = null
    ): BeatInterval

    fun findCurrentIntervalType(strategy: DomainFindingStrategy): IntervalType

    fun findCurrentInterval(strategy: DomainFindingStrategy): BeatInterval

    fun findAllIntervals(strategy: DomainFindingStrategy, type: IntervalType?): List<BeatInterval>
}