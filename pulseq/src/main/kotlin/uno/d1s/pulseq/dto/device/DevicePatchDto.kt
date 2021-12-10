package uno.d1s.pulseq.dto.device

import javax.validation.constraints.NotEmpty

data class DevicePatchDto(
    @NotEmpty val deviceName: String
)