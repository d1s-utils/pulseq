package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import uno.d1s.pulseq.controller.advice.ExceptionHandlerControllerAdvice
import uno.d1s.pulseq.controller.impl.DeviceControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.exception.impl.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.impl.DeviceNotFoundException
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.byAll
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.util.HttpServletResponseUtil
import uno.d1s.pulseq.util.expectJsonContentType

@WebMvcTest(useDefaultFilters = false, controllers = [DeviceControllerImpl::class])
@ContextConfiguration(classes = [DeviceControllerImpl::class, ExceptionHandlerControllerAdvice::class, HttpServletResponseUtil::class])
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
            deviceService.findDevice(byAll(INVALID_STUB))
        } throws DeviceNotFoundException()

        every {
            deviceService.registerNewDevice(VALID_STUB)
        } returns testDevice

        every {
            deviceService.registerNewDevice(INVALID_STUB)
        } throws DeviceAlreadyExistsException()

        every {
            deviceService.findDeviceBeats(byAll(VALID_STUB))
        } returns testBeats

        every {
            deviceService.findDeviceBeats(byAll(INVALID_STUB))
        } throws DeviceNotFoundException()

        every {
            deviceDtoConverter.convertToDto(testDevice)
        } returns testDeviceDto

        every {
            deviceDtoConverter.convertToDtoList(testDevices)
        } returns testDevicesDto

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

        verifyDevicesConversion()
    }

    @Test
    fun `should return 200 and valid device on getting device by identify`() {
        getByIdentifyAndExpect(VALID_STUB) {
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
    fun `should return 404 on getting device with invalid identify`() {
        getByIdentifyAndExpect(INVALID_STUB) {
            status {
                isNotFound()
            }
        }

        verify {
            deviceService.findDevice(byAll(INVALID_STUB))
        }
    }

    @Test
    fun `should return 201 and valid device on device registration`() {
        registerDeviceAndExpect(VALID_STUB) {
            status {
                isCreated()
            }

            expectDeviceDto()

            expectJsonContentType()
        }

        verify {
            deviceService.registerNewDevice(VALID_STUB)
        }

        verifyDeviceConversion()
    }

    @Test
    fun `should return 409 on device registration with existing name`() {
        registerDeviceAndExpect(INVALID_STUB) {
            status {
                isConflict()
            }
        }

        verify {
            deviceService.registerNewDevice(INVALID_STUB)
        }
    }

    private fun MockMvcResultMatchersDsl.expectDeviceDto() {
        content {
            json(objectMapper.writeValueAsString(testDeviceDto))
        }
    }

    @Test
    fun `should return 200 and valid list on getting beats by device identify`() {
        getBeatsByDeviceIdentifyAndExpect(VALID_STUB) {
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
    fun `should return 404 on getting beats by invalid device identify`() {
        getBeatsByDeviceIdentifyAndExpect(INVALID_STUB) {
            status {
                isNotFound()
            }
        }

        verify {
            deviceService.findDeviceBeats(byAll(INVALID_STUB))
        }
    }

    private fun getBeatsByDeviceIdentifyAndExpect(id: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(DeviceMappingConstants.GET_BEATS.replacePathPlaceholder("identify", id)).andExpect(block)
    }

    private fun getByIdentifyAndExpect(identify: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(DeviceMappingConstants.GET_DEVICE_BY_IDENTIFY.replacePathPlaceholder("identify", identify))
            .andExpect(block)
    }

    private fun registerDeviceAndExpect(name: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.post(DeviceMappingConstants.REGISTER_DEVICE) {
            param("deviceName", name)
        }.andExpect(block)
    }

    private fun verifyDeviceConversion() {
        verify {
            deviceDtoConverter.convertToDto(testDevice)
        }
    }

    private fun verifyDevicesConversion() {
        verify {
            deviceDtoConverter.convertToDtoList(testDevices)
        }
    }
}