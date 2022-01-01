/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.SimpleTimeSpan
import uno.d1s.pulseq.strategy.DomainFindingStrategy
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy
import java.time.Instant

interface BeatService {

    fun findById(id: String): Beat

    fun findFirst(strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll): Beat

    fun findLast(strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll): Beat

    fun findAll(strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll): List<Beat>

    fun findAll(ids: List<String>): List<Beat>

    fun findAllCreatedIn(
        timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll
    ): List<Beat>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat>

    fun findAllCreatedInByBeats(timeSpan: SimpleTimeSpan, beats: List<Beat>): List<Beat>

    fun remove(id: String): Beat

    fun remove(beat: Beat): Beat

    fun removeAll(ids: List<String>): List<Beat>

    fun removeAllByBeats(beats: List<Beat>): List<Beat>

    fun removeAll(strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll): List<Beat>

    fun removeAllCreatedIn(
        timeSpan: SimpleTimeSpan, strategy: DomainFindingStrategy = DomainFindingStrategy.ByAll
    ): List<Beat>

    fun removeAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Beat>

    fun create(beat: Beat): Beat

    fun create(strategy: SourceFindingStrategy): Beat

    fun create(instant: Instant, strategy: SourceFindingStrategy)

    fun createAll(strategies: List<SourceFindingStrategy>): List<Beat>

    fun createAll(instants: List<Instant>, strategies: List<SourceFindingStrategy>)

    fun createAll(strategy: SourceFindingStrategy, count: Long)
}