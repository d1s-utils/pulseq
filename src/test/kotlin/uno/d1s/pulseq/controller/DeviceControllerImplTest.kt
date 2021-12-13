package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.*
import uno.d1s.pulseq.controller.impl.DeviceControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.util.replacePathPlaceholder
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.byAll
import uno.d1s.pulseq.testUtils.*

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [DeviceControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [DeviceControllerImpl::class])
internal class DeviceControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var deviceService: DeviceService

    @MockkBean
    private lateinit var deviceDtoConverter: DtoConverter<Device, DeviceDto>

    @MockkBean
    private lateinit var devicePatchDtoConverter: DtoConverter<Device, DevicePatchDto>

    @MockkBean
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @BeforeEach
    fun setup() {
        every {
            deviceService.findAllRegisteredDevices()
        } returns testDevices

        every {
            deviceService.findDevice(byAll(VALID_STUB))
        } returns testDevice

        every {
            deviceService.registerNewDevice(any())
        } returns testDevice

        every {
            deviceService.findDeviceBeats(byAll(VALID_STUB))
        } returns testBeats

        every {
            deviceService.updateDevice(byAll(VALID_STUB), testDeviceUpdate)
        } returns testDeviceUpdate

        every {
            deviceDtoConverter.convertToDto(testDevice)
        } returns testDeviceDto

        every {
            deviceDtoConverter.convertToDto(testDeviceUpdate)
        } returns testDeviceUpdateDto

        every {
            deviceDtoConverter.convertToDtoList(testDevices)
        } returns testDevicesDto

        every {
            devicePatchDtoConverter.convertToDomain(testDevicePatchDto)
        } returns testDeviceUpdate

        every {
            beatDtoConverter.convertToDtoList(testBeats)
        } returns testBeatsDto
    }

    @Test
    fun `should return 200 and valid list on getting all devices`() {
        mockMvc.get(DeviceMappingConstants.GET_ALL_DEVICES).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testDevicesDto))
            }

            expectJsonContentType()
        }

        verify {
            deviceService.findAllRegisteredDevices()
        }

        verify {
            deviceDtoConverter.convertToDtoList(testDevices)
        }
    }

    @Test
    fun `should return 200 and valid device on getting device by identify`() {
        mockMvc.get(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY.replaceIdentify())
            .andExpect {
                status {
                    isOk()
                }

                expectDeviceDto()

                expectJsonContentType()
            }

        verify {
            deviceService.findDevice(byAll(VALID_STUB))
        }

        verifyDeviceConversion()
    }

    @Test
    fun `should return 201 and valid device on device registration`() {
        mockMvc.post(DeviceMappingConstants.REGISTER_DEVICE) {
            content = objectMapper.writeValueAsString(testDevicePatchDto)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isCreated()
            }

            expectDeviceDto()

            expectJsonContentType()
        }

        verify {
            deviceService.registerNewDevice(any())
        }

        verifyDeviceConversion()
    }

    private fun MockMvcResultMatchersDsl.expectDeviceDto() {
        content {
            json(objectMapper.writeValueAsString(testDeviceDto))
        }
    }

    @Test
    fun `should return 200 and valid list on getting beats by device identify`() {
        mockMvc.get(DeviceMappingConstants.GET_BEATS.replaceIdentify()).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testBeatsDto))
            }

            expectJsonContentType()
        }

        verify {
            deviceService.findDeviceBeats(byAll(VALID_STUB))
        }

        verify {
            beatDtoConverter.convertToDtoList(testBeats)
        }
    }

    @Test
    fun `should return 202 and patch the device`() {
        mockMvc.patch(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY.replaceIdentify()) {
            content = objectMapper.writeValueAsString(testDevicePatchDto)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isAccepted()
            }

            expectJsonContentType()

            content {
                json(objectMapper.writeValueAsString(testDeviceUpdateDto))
            }
        }

        verify {
            deviceService.updateDevice(byAll(VALID_STUB), testDeviceUpdate)
        }

        verify {
            devicePatchDtoConverter.convertToDomain(testDevicePatchDto)
        }
    }

    private fun verifyDeviceConversion() {
        verify {
            deviceDtoConverter.convertToDto(testDevice)
        }
    }

    private fun String.replaceIdentify() =
        this.replacePathPlaceholder("identify", VALID_STUB)
}