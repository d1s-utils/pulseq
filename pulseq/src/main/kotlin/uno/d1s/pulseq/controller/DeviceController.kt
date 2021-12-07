package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategyType
import javax.validation.constraints.NotEmpty

@Validated
interface DeviceController {

    @GetMapping(DeviceMappingConstants.GET_ALL_DEVICES)
    fun getAllDevices(): ResponseEntity<List<DeviceDto>>

    @GetMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun getDeviceByIdentify(
        @PathVariable @NotEmpty identify: String,
        @RequestParam(required = false, name = "strategy") findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<DeviceDto>

    @PostMapping(DeviceMappingConstants.REGISTER_DEVICE)
    fun registerNewDevice(@RequestParam @NotEmpty deviceName: String): ResponseEntity<DeviceDto>

    @GetMapping(DeviceMappingConstants.GET_BEATS)
    fun getDeviceBeats(
        @PathVariable @NotEmpty identify: String,
        @RequestParam(required = false, name = "strategy") findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<List<BeatDto>>
}