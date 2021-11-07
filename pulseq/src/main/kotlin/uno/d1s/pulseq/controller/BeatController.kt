package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.core.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.dto.BeatDto
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotEmpty

@Validated
interface BeatController {

    @GetMapping(BeatMappingConstants.GET_BEAT_BY_ID)
    fun getBeatBtId(@PathVariable @NotEmpty id: String): ResponseEntity<BeatDto>

    @RequestMapping(BeatMappingConstants.BASE, method = [RequestMethod.POST, RequestMethod.GET])
    fun registerNewBeatWithDeviceIdentify(
        @RequestParam("device", required = false) deviceParam: String?,
        @RequestHeader("Device", required = false) deviceHeader: String?,
        response: HttpServletResponse
    ): ResponseEntity<BeatDto>?

    @GetMapping(BeatMappingConstants.GET_BEATS_BY_DEVICE_IDENTIFY)
    fun getBeatsByDeviceIdentify(@PathVariable @NotEmpty identify: String): ResponseEntity<List<BeatDto>>

    @GetMapping(BeatMappingConstants.GET_BEATS)
    fun getBeats(): ResponseEntity<List<BeatDto>>

    @GetMapping(BeatMappingConstants.LAST_BEAT)
    fun getLastBeat(): ResponseEntity<BeatDto>
}