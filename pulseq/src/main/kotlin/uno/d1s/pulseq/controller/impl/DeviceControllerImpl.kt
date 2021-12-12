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
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategyType
import uno.d1s.pulseq.strategy.device.byStrategyType
import javax.validation.Valid

@RestController
class DeviceControllerImpl : DeviceController {

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var deviceDtoConverter: DtoConverter<Device, DeviceDto>

    @Autowired
    private lateinit var devicePatchDtoConverter: DtoConverter<Device, DevicePatchDto>

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
                findingStrategy.thisStrategyOrAll(identify)
            )
        )
    )

    override fun registerNewDevice(@Valid device: DevicePatchDto): ResponseEntity<DeviceDto> {
        val createdDevice = deviceDtoConverter.convertToDto(
            deviceService.registerNewDevice(device.deviceName)
        )

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(createdDevice.id!!).toUri()
        ).body(createdDevice)
    }

    override fun getDeviceBeats(
        identify: String, findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<List<BeatDto>> = ResponseEntity.ok(
        beatDtoConverter.convertToDtoList(
            deviceService.findDeviceBeats(findingStrategy.thisStrategyOrAll(identify))
        )
    )

    override fun patchDevice(
        identify: String, findingStrategy: DeviceFindingStrategyType?, patch: DevicePatchDto
    ): ResponseEntity<DeviceDto> = ResponseEntity.accepted().body(
        deviceDtoConverter.convertToDto(
            deviceService.updateDevice(
                findingStrategy.thisStrategyOrAll(identify), devicePatchDtoConverter.convertToDomain(patch)
            )
        )
    )

    override fun deleteDevice(identify: String, findingStrategy: DeviceFindingStrategyType?): ResponseEntity<Any> {
        deviceService.deleteDevice(findingStrategy.thisStrategyOrAll(identify))
        return ResponseEntity.noContent().build()
    }

    private fun DeviceFindingStrategyType?.thisStrategyOrAll(identify: String) =
        byStrategyType(identify, this ?: DeviceFindingStrategyType.BY_ALL)
}