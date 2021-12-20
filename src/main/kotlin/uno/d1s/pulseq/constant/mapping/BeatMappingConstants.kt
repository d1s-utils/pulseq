/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.constant.mapping

object BeatMappingConstants {
    const val BASE = "/beats"
    const val GET_BEAT_BY_ID = "$BASE/{id}"
    const val GET_BEATS = BASE
    const val LAST_BEAT = "$BASE/last"
}