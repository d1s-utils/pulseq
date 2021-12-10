package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategyType
import javax.validation.Valid
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
    @PreAuthorize("@authenticationService.authenticatedRequest")
    fun registerNewDevice(@RequestParam @NotEmpty deviceName: String): ResponseEntity<DeviceDto>

    @GetMapping(DeviceMappingConstants.GET_BEATS)
    fun getDeviceBeats(
        @PathVariable @NotEmpty identify: String,
        @RequestParam(required = false, name = "strategy") findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<List<BeatDto>>

    @PreAuthorize("@authenticationService.authenticatedRequest")
    @PatchMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun patchDevice(
        @PathVariable @NotEmpty identify: String,
        findingStrategy: DeviceFindingStrategyType?,
        @RequestBody @Valid patch: DevicePatchDto
    ): ResponseEntity<DeviceDto>

    @PreAuthorize("@authenticationService.authenticatedRequest")
    @DeleteMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun deleteDevice(
        @PathVariable @NotEmpty identify: String,
        findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<Any>
}