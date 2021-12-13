package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.service.BeatService

@Component
class DeviceDtoConverter : DtoConverterFacade<Device, DeviceDto>() {

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @Autowired
    private lateinit var beatService: BeatService

    override fun convertToDto(domain: Device): DeviceDto = DeviceDto(domain.name).apply {
        domain.id?.let {
            id = it

            domain.beats?.let { beatList ->
                beats = beatDtoConverter.convertToDtoList(beatList).map { beat ->
                    beat.id!!
                }
            }
        }
    }

    override fun convertToDomain(dto: DeviceDto): Device = Device(dto.deviceName).apply {
        dto.id?.let { dtoId ->
            id = dtoId
            dto.beats?.let { beatList ->
                beats = beatList.map {
                    beatService.findBeatById(it)
                }
            }
        }
    }
}