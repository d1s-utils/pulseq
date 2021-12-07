package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import uno.d1s.pulseq.controller.DeviceController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategyType
import uno.d1s.pulseq.strategy.device.byStrategyType

@RestController
class DeviceControllerImpl : DeviceController {

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var deviceDtoConverter: DtoConverter<Device, DeviceDto>

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    override fun getAllDevices(): ResponseEntity<List<DeviceDto>> = ResponseEntity.ok(
        deviceDtoConverter.convertToDtoList(
            deviceService.findAllRegisteredDevices()
        )
    )

    override fun getDeviceByIdentify(
        identify: String, findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<DeviceDto> = ResponseEntity.ok(
        deviceDtoConverter.convertToDto(
            deviceService.findDevice(
                byStrategyType(
                    identify, findingStrategy ?: DeviceFindingStrategyType.BY_ALL
                )
            )
        )
    )

    override fun registerNewDevice(deviceName: String): ResponseEntity<DeviceDto> {
        val createdDevice = deviceDtoConverter.convertToDto(
            deviceService.registerNewDevice(deviceName)
        )

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(createdDevice.id!!).toUri()
        ).body(createdDevice)
    }

    override fun getDeviceBeats(
        identify: String, findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<List<BeatDto>> = ResponseEntity.ok(
        beatDtoConverter.convertToDtoList(
            deviceService.findDeviceBeats(byStrategyType(identify, findingStrategy ?: DeviceFindingStrategyType.BY_ALL))
        )
    )
}