/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.constant.mapping

object SourceMappingConstants {
    private const val BASE = "/sources"
    const val GET_SOURCE_BY_IDENTIFY = "$BASE/{identify}"
    const val GET_ALL_SOURCES = BASE
    const val REGISTER_SOURCE = BASE
    const val GET_BEATS = "$BASE/{identify}/beats"
}