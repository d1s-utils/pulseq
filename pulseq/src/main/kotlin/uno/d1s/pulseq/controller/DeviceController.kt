package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.dto.DeviceDto

interface DeviceController {

    @GetMapping(DeviceMappingConstants.GET_ALL_DEVICES)
    fun getAllDevices(): ResponseEntity<List<DeviceDto>>

    @GetMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun getDeviceByIdentify(@PathVariable identify: String): ResponseEntity<DeviceDto>
}