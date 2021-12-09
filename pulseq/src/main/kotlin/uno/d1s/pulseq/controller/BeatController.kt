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

    @PostMapping(BeatMappingConstants.BASE)
    fun registerNewBeatWithDeviceIdentify(
        @RequestParam("device", required = false) deviceParam: String?,
        @RequestHeader("Device", required = false) deviceHeader: String?,
        response: HttpServletResponse
    ): ResponseEntity<BeatDto>?

    @GetMapping(BeatMappingConstants.GET_BEATS)
    fun getBeats(): ResponseEntity<List<BeatDto>>

    @GetMapping(BeatMappingConstants.LAST_BEAT)
    fun getLastBeat(): ResponseEntity<BeatDto>

    @DeleteMapping(BeatMappingConstants.GET_BEAT_BY_ID)
    fun deleteBeat(@PathVariable @NotEmpty id: String): ResponseEntity<Any>
}