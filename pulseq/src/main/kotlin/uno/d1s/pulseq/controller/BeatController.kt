package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.core.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.dto.BeatDto
import javax.servlet.http.HttpServletResponse

interface BeatController {

    @RequestMapping(BeatMappingConstants.BASE, method = [RequestMethod.POST, RequestMethod.GET])
    fun registerNewBeatWithDeviceIdentify(
        @RequestParam(required = false) device: String?,
        response: HttpServletResponse
    ): ResponseEntity<BeatDto>?

    @GetMapping(BeatMappingConstants.GET_BEATS_BY_DEVICE_IDENTIFY)
    fun getBeatsByDeviceIdentify(@PathVariable identify: String): ResponseEntity<List<BeatDto>>

    @GetMapping(BeatMappingConstants.GET_BEATS)
    fun getBeats(): ResponseEntity<List<BeatDto>>

    @GetMapping(BeatMappingConstants.LAST_BEAT)
    fun getLastBeat(): ResponseEntity<BeatDto>
}