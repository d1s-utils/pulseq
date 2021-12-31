/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.constant.mapping

object ActivityMappingConstants {
    private const val BASE = "/timespans"
    const val GET_TIMESPANS = BASE
    const val GET_CURRENT_TIMESPAN = "$BASE/current"
    const val GET_LAST_TIMESPAN = "$BASE/last"
    const val GET_LONGEST_TIME_SPAN = "$BASE/longest"
}