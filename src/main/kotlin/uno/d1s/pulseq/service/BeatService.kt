/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.Count
import uno.d1s.pulseq.dto.SimpleTimeSpan
import uno.d1s.pulseq.strategy.DomainFindingStrategy
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import java.time.Duration
import java.time.Instant

interface BeatService {

    fun findById(id: String, strategy: DomainFindingStrategy): Beat

    fun findFirst(): Beat

    fun findLast(): Beat

    fun findAll(strategy: DomainFindingStrategy): List<Beat>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan): List<Beat>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy): List<Beat>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat>

    fun findAverageCountForDuration(duration: Duration): Count

    fun findMaxCountForDuration(duration: Duration): Count

    fun remove(id: String, sendEvent: Boolean = true): Beat

    fun removeAll(): List<Beat>

    fun removeAll(ids: List<String>): List<Beat>

    fun removeAll(strategy: DomainFindingStrategy): List<Beat>

    fun removeAllCreatedIn(timeSpan: SimpleTimeSpan): List<Beat>

    fun removeAllCreatedIn(timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy): List<Beat>

    fun removeAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat>

    fun create(strategy: SourceFindingStrategy): Beat

    fun create(instant: Instant, strategy: SourceFindingStrategy)

    fun createAll(strategies: List<SourceFindingStrategy>): List<Beat>

    fun createAll(instants: List<Instant>, strategies: List<SourceFindingStrategy>)

    fun createAll(strategy: DomainFindingStrategy, id: String, count: Long)
}