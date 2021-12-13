package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.ErrorDto
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategyType
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Validated
@Tag(name = "Devices", description = "Manage registered devices.")
interface DeviceController {

    @Operation(summary = "Get all registered devices.")
    @ApiResponses(
        ApiResponse(
            description = "Found devices.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = DeviceDto::class))
            )]
        )
    )
    @GetMapping(DeviceMappingConstants.GET_ALL_DEVICES)
    fun getAllDevices(): ResponseEntity<List<DeviceDto>>

    @Operation(summary = "Get the device by specified identify.")
    @ApiResponses(
        ApiResponse(
            description = "Found the device.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = DeviceDto::class)
            )]
        ), ApiResponse(
            description = "The device was not found.", responseCode = "404", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun getDeviceByIdentify(
        @PathVariable @Parameter(
            description = "Device identify associated with device to search for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The device finding strategy to use."
        ) findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<DeviceDto>


    @Operation(
        summary = "Register new device.",
        description = "This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Registered the device", responseCode = "201", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = DeviceDto::class)
            )]
        ), ApiResponse(
            description = "The device with the same name already exists.", responseCode = "409", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
    )
    @PostMapping(
        DeviceMappingConstants.REGISTER_DEVICE,
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    fun registerNewDevice(
        @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The device information for the new device to be registered.", content = [Content(
                schema = Schema(implementation = DeviceDto::class)
            )]
        ) @Valid device: DevicePatchDto
    ): ResponseEntity<DeviceDto>

    @Operation(summary = "Get device beats.")
    @ApiResponses(
        ApiResponse(
            description = "Found the device beats.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = ErrorDto::class))
            )]
        ), ApiResponse(
            description = "The device was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
    )
    @GetMapping(DeviceMappingConstants.GET_BEATS)
    fun getDeviceBeats(
        @PathVariable @Parameter(
            description = "Device identify associated with device to search for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The device finding strategy to use."
        ) findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<List<BeatDto>>

    @Operation(
        summary = "Edit the device.",
        description = "This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Edited the device",
            responseCode = "202",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        ), ApiResponse(
            description = "The device was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The device with the same name already exists.", responseCode = "409", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    @PatchMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun patchDevice(
        @PathVariable @Parameter(
            description = "Device identify associated with device to edit for."
        ) @NotEmpty identify: String,
        @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The device finding strategy to use."
        ) findingStrategy: DeviceFindingStrategyType?,
        @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The device information for the new device to be edited.") @Valid patch: DevicePatchDto
    ): ResponseEntity<DeviceDto>

    @Operation(
        summary = "Delete the device.",
        description = "Note: this operation will delete all the beats associated with the device to be deleted. This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Deleted the device.", responseCode = "200"
        ), ApiResponse(
            description = "The device was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    @DeleteMapping(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY)
    fun deleteDevice(
        @PathVariable @Parameter(
            description = "Device identify associated with device to delete for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The device finding strategy to use."
        ) findingStrategy: DeviceFindingStrategyType?
    ): ResponseEntity<Any>
}