package uno.d1s.pulseq.core.constant.mapping

object DeviceMappingConstants {
    @Suppress("MemberVisibilityCanBePrivate")
    const val BASE = "${GlobalMappingConstants.API}/device"
    const val GET_DEVICE_BY_IDENTIFY = "$BASE/identify/{identify}"
    const val GET_ALL_DEVICES = "$BASE/list"
    const val REGISTER_DEVICE = "$BASE/new"
}