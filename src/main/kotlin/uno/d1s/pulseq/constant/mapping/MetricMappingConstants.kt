/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.constant.mapping

@Suppress("unused") // this class is about to be needed in the future.
object MetricMappingConstants {
    private const val BASE = "/metric"
    const val GET_METRIC_BY_IDENTIFY = "$BASE/{identify}"
}