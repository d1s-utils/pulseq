package uno.d1s.pulseq.converter.impl

import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterAdapter
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.device.DevicePatchDto

@Component
class DevicePatchDtoConverter : DtoConverterAdapter<Device, DevicePatchDto>() {

    override fun convertToDto(domain: Device): DevicePatchDto = DevicePatchDto(
        domain.name
    )

    override fun convertToDomain(dto: DevicePatchDto): Device = Device(
        dto.deviceName!!
    )
}