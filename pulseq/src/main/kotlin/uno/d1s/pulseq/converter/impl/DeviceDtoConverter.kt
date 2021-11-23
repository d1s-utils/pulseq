package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.service.BeatService

@Component
class DeviceDtoConverter : DtoConverter<Device, DeviceDto> {

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @Autowired
    private lateinit var beatService: BeatService

    override fun convertToDto(domain: Device): DeviceDto =
        DeviceDto(domain.name).apply {
            domain.id?.let {
                id = it

                domain.beats?.let { beatList ->
                    beats = beatDtoConverter.convertToDtoList(beatList).map {
                        domain.id!!
                    }
                }
            }
        }

    override fun convertToDomain(dto: DeviceDto): Device =
        Device(dto.deviceName).apply {
            dto.id?.let { dtoId ->
                id = dtoId
                dto.beats?.let { beatList ->
                    beats = beatList.map {
                        beatService.findBeatById(it)
                    }
                }
            }
        }

    override fun convertToDtoList(domains: List<Device>): List<DeviceDto> =
        domains.map {
            this.convertToDto(it)
        }

    override fun convertToDomainList(dtoList: List<DeviceDto>): List<Device> =
        dtoList.map {
            this.convertToDomain(it)
        }
}