/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.constant.mapping

object ActivityMappingConstants {
    private const val BASE = "/durations"
    const val GET_DURATIONS = BASE
    const val GET_CURRENT_DURATION = "$BASE/current"
    const val GET_LAST_DURATION = "$BASE/last"
    const val GET_LONGEST_DURATION = "$BASE/longest"
}