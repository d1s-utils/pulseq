package uno.d1s.pulseq.constant.mapping

object DeviceMappingConstants {
    const val BASE = "/devices"
    const val GET_DEVICE_BY_IDENTIFY = "$BASE/{identify}"
    const val GET_ALL_DEVICES = BASE
    const val REGISTER_DEVICE = BASE
    const val GET_BEATS = "$BASE/{identify}/beats"
}