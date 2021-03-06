/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy

interface SourceService {

    fun findAllRegisteredSources(): List<Source>

    fun findSource(
        strategy: SourceFindingStrategy
    ): Source

    fun registerNewSource(name: String): Source

    fun updateSource(strategy: SourceFindingStrategy, source: Source): Source

    fun deleteSource(strategy: SourceFindingStrategy): Source

    fun findSourceBeats(strategy: SourceFindingStrategy): List<Beat>
}