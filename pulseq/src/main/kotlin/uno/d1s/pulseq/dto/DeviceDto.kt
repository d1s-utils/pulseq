package uno.d1s.pulseq.dto

class DeviceDto(
    val deviceName: String
) {
    var id: String? = null
    var beats: List<String>? = null
}