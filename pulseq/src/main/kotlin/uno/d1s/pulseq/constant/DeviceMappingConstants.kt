package uno.d1s.pulseq.constant

object DeviceMappingConstants {
    @Suppress("MemberVisibilityCanBePrivate")
    const val BASE = "${GlobalMappingConstants.API}/device"
    const val GET_DEVICE_BY_IDENTIFY = "$BASE/{identify}"
    const val GET_ALL_DEVICES = "$BASE/list"
}