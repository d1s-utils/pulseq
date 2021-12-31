/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.aspect.cache.idProvider.IdListProvider
import uno.d1s.pulseq.aspect.cache.idProvider.IdProvider
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.dto.source.SourceDto
import uno.d1s.pulseq.dto.source.SourcePatchDto
import java.time.Duration
import java.time.Instant

// constants
internal const val VALID_STUB = "valid_stub"
internal const val INVALID_STUB = "invalid_stub"

// sources
internal val testSource = Source(VALID_STUB).apply {
    id = VALID_STUB
}
internal val testSources = listOf(testSource)
internal val testSourceDto = SourceDto(VALID_STUB).apply {
    id = VALID_STUB
    beats = listOf(VALID_STUB)
}
internal val testSourcesDto = listOf(testSourceDto)
internal val testSourceUpdate = Source("new-name").apply {
    id = testSource.id
    beats = testSource.beats
}
internal val testSourceUpdates = listOf(testSourceUpdate)
internal val testSourceUpdateDto = SourceDto(testSourceUpdate.name).apply {
    id = testSourceUpdate.id
    beats = listOf(VALID_STUB)
}
internal val testSourcePatchDto = SourcePatchDto("new-name")
internal val testSourcePatchesDto = listOf(testSourcePatchDto)


// beats
internal val testBeat = Beat(testSource, Duration.ZERO, Instant.EPOCH).apply {
    id = VALID_STUB

    source.beats = listOf(this)
}
internal val testBeats = listOf(testBeat)
internal val testBeatDto = BeatDto(VALID_STUB, Instant.EPOCH, Duration.ZERO).apply {
    id = VALID_STUB
}
internal val testBeatsDto = listOf(testBeatDto)

// time spans
internal val testTimeSpan =
    TimeSpan(Duration.ZERO, TimeSpanType.ACTIVITY, testBeat, testBeat)
internal val testTimeSpans = listOf(testTimeSpan)
internal val testTimeSpanDto = TimeSpanDto(
    testTimeSpan.duration,
    testTimeSpan.type,
    testTimeSpan.startBeat.id!!,
    testTimeSpan.endBeat!!.id!!
)
internal val testTimeSpansDto = listOf(testTimeSpanDto)

// collections
internal val testCollection = listOf(1, 2, 3)

// id providers
internal val testIdProvider = object : IdProvider {
    override fun getId(any: Any) = VALID_STUB
}

// id list providers
internal val testIdListProvider = object : IdListProvider {
    override fun getIdList(any: Any) = listOf(VALID_STUB)

}