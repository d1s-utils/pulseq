/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat

interface BeatService {

    fun findBeatById(id: String): Beat

    fun registerNewBeatWithSourceIdentify(identify: String): Beat

    fun findAllBeats(): List<Beat>

    fun totalBeats(): Int

    fun totalBeatsBySources(): Map<String, Int>

    fun findLastBeat(): Beat

    fun findFirstBeat(): Beat

    fun deleteBeat(id: String, sendEvent: Boolean = true)
}