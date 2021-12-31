/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.aspect.cache.idProvider.IdListProvider
import uno.d1s.pulseq.aspect.cache.idProvider.IdProvider
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.dto.beat.BeatDto
import uno.d1s.pulseq.dto.beat.BeatIntervalDto
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
internal val testBeatInterval =
    BeatInterval(Duration.ZERO, IntervalType.ACTIVITY, testBeat, testBeat)
internal val testDurations = listOf(testBeatInterval)
internal val testBeatIntervalDto = BeatIntervalDto(
    testBeatInterval.duration,
    testBeatInterval.type,
    testBeatInterval.start.id!!,
    testBeatInterval.end!!.id!!
)
internal val testDurationsDto = listOf(testBeatIntervalDto)

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