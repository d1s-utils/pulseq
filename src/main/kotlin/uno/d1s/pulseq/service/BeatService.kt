package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.strategy.source.SourceFindingStrategy

interface BeatService {

    fun findBeatById(id: String): Beat

    fun registerNewBeatWithSourceIdentify(identify: String): Beat

    fun findAllBySource(
        strategy: SourceFindingStrategy
    ): List<Beat>

    fun findAllBeats(): List<Beat>

    fun totalBeats(): Int

    fun totalBeatsBySources(): Map<String, Int>

    fun findLastBeat(): Beat

    fun findFirstBeat(): Beat

    fun deleteBeat(id: String, sendEvent: Boolean = true)
}