package uno.d1s.pulseq.core.constant.mapping

object BeatMappingConstants {
    const val BASE = "${GlobalMappingConstants.API}/beat"
    const val GET_BEAT_BY_ID = "$BASE/id/{id}"
    const val GET_BEATS_BY_DEVICE_IDENTIFY = "$BASE/device/{identify}"
    const val GET_BEATS = "$BASE/list"
    const val LAST_BEAT = "$BASE/last"
}