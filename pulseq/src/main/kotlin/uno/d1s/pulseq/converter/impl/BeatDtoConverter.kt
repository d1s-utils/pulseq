package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.byId

@Component
class BeatDtoConverter : DtoConverterFacade<Beat, BeatDto>() {

    @Autowired
    private lateinit var deviceService: DeviceService

    override fun convertToDto(domain: Beat): BeatDto = BeatDto(
        domain.device.id ?: throw IllegalArgumentException("Device id could not be null."),
        domain.beatTime,
        domain.inactivityBeforeBeat
    ).apply {
        domain.id?.let {
            id = it
        }
    }

    override fun convertToDomain(dto: BeatDto): Beat = Beat(
        deviceService.findDevice(byId(dto.device)), dto.inactivityBeforeBeat, dto.beatTime
    ).apply {
        dto.id?.let {
            id = it
        }
    }
}