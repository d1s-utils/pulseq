package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.BeatController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.BeatService
import javax.servlet.http.HttpServletResponse


@RestController
class BeatControllerImpl : BeatController {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    override fun getBeatBtId(id: String): ResponseEntity<BeatDto> =
        ResponseEntity.ok(
            beatDtoConverter.convertToDto(
                beatService.findBeatById(id)
            )
        )

    override fun registerNewBeatWithDeviceIdentify(
        device: String?,
        response: HttpServletResponse
    ): ResponseEntity<BeatDto>? {
        device ?: run {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Device name is not present.")
            return null
        }

        return ResponseEntity.ok(
            beatDtoConverter.convertToDto(
                beatService.registerNewBeatWithDeviceIdentify(device)
            )
        )
    }

    override fun getBeatsByDeviceIdentify(identify: String): ResponseEntity<List<BeatDto>> =
        ResponseEntity.ok(
            beatDtoConverter.convertToDtoList(
                beatService.findAllBeatsByDeviceIdentify(identify)
            )
        )

    override fun getBeats(): ResponseEntity<List<BeatDto>> =
        ResponseEntity.ok(
            beatDtoConverter.convertToDtoList(
                beatService.findAllBeats()
            )
        )

    override fun getLastBeat(): ResponseEntity<BeatDto> =
        ResponseEntity.ok(
            beatDtoConverter.convertToDto(
                beatService.findLastBeat()
            )
        )
}