package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.service.DeviceService

@Component
class BeatDtoConverter : DtoConverter<Beat, BeatDto> {

    @Autowired
    private lateinit var deviceDtoConverter: DtoConverter<Device, DeviceDto>

    @Autowired
    private lateinit var deviceService: DeviceService

    override fun convertToDto(domain: Beat): BeatDto =
        BeatDto(
            deviceDtoConverter.convertToDto(domain.device).id!!,
            domain.beatTime,
            domain.inactivityBeforeBeat
        ).apply {
            domain.id?.let {
                id = it
            }
        }

    override fun convertToDomain(dto: BeatDto): Beat =
        Beat(deviceService.findDeviceById(dto.device), dto.inactivityBeforeBeat, dto.beatTime).apply {
            dto.id?.let {
                id = it
            }
        }

    override fun convertToDtoList(domains: List<Beat>): List<BeatDto> =
        domains.map {
            this.convertToDto(it)
        }

    override fun convertToDomainList(dtoList: List<BeatDto>): List<Beat> =
        dtoList.map {
            this.convertToDomain(it)
        }
}