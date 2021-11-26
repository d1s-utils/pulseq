package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.dto.DeviceDto
import javax.validation.constraints.NotEmpty

@Validated
interface DeviceController {

    @GetMapping(DeviceMappingConstants.GET_ALL_DEVICES)
    fun getAllDevices(): ResponseEntity<List<DeviceDto>>

    @GetMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun getDeviceByIdentify(@PathVariable @NotEmpty identify: String): ResponseEntity<DeviceDto>

    @PostMapping(DeviceMappingConstants.REGISTER_DEVICE)
    fun registerNewDevice(@RequestParam @NotEmpty deviceName: String): ResponseEntity<DeviceDto>
}