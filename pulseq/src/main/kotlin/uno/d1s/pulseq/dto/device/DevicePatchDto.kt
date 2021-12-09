package uno.d1s.pulseq.dto.device

import javax.validation.constraints.NotEmpty

class DevicePatchDto(
    @NotEmpty val deviceName: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DevicePatchDto

        if (deviceName != other.deviceName) return false

        return true
    }

    override fun hashCode(): Int {
        return deviceName.hashCode()
    }

    override fun toString(): String {
        return "DevicePatchDto(deviceName=$deviceName)"
    }
}