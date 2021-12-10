package uno.d1s.pulseq.dto.device

class DeviceDto(
    val deviceName: String
) {
    var id: String? = null
    var beats: List<String>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceDto

        if (deviceName != other.deviceName) return false
        if (beats != other.beats) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceName.hashCode()
        result = 31 * result + (beats?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DeviceDto(deviceName='$deviceName', id=$id, beats=$beats)"
    }
}