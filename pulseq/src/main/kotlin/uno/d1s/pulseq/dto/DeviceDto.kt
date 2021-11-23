package uno.d1s.pulseq.dto

import javax.validation.constraints.NotEmpty

class DeviceDto(
    @NotEmpty val deviceName: String
) {
    var id: String? = null
    var beats: List<String>? = null
}