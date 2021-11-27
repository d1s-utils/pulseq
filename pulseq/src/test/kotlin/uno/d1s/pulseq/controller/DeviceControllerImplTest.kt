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
import uno.d1s.pulseq.*
import uno.d1s.pulseq.controller.advice.ExceptionHandlerControllerAdvice
import uno.d1s.pulseq.controller.impl.DeviceControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.core.constant.mapping.DeviceMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.exception.DeviceAlreadyExistsException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.util.expectJsonContentType

@WebMvcTest(useDefaultFilters = false, controllers = [DeviceControllerImpl::class])
@ContextConfiguration(classes = [DeviceControllerImpl::class, ExceptionHandlerControllerAdvice::class])
internal class DeviceControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var deviceService: DeviceService

    @MockkBean
    private lateinit var deviceDtoConverter: DtoConverter<Device, DeviceDto>

    @BeforeEach
    fun setup() {
        every {
            deviceService.findAllRegisteredDevices()
        } returns testDevices

        every {
            deviceService.findDeviceByIdentify(VALID_STUB)
        } returns testDevice

        every {
            deviceService.findDeviceByIdentify(INVALID_STUB)
        } throws DeviceNotFoundException()

        every {
            deviceService.registerNewDevice(VALID_STUB)
        } returns testDevice

        every {
            deviceService.registerNewDevice(INVALID_STUB)
        } throws DeviceAlreadyExistsException()

        every {
            deviceDtoConverter.convertToDto(testDevice)
        } returns testDeviceDto

        every {
            deviceDtoConverter.convertToDtoList(testDevices)
        } returns testDevicesDto
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
            deviceService.findDeviceByIdentify(VALID_STUB)
        }

        verifyDeviceConversion()
    }

    @Test
    fun `should return 400 on getting device with invalid identify`() {
        getByIdentifyAndExpect(INVALID_STUB) {
            status {
                isBadRequest()
            }
        }

        verify {
            deviceService.findDeviceByIdentify(INVALID_STUB)
        }
    }

    @Test
    fun `should return 200 and valid device on device registration`() {
        registerDeviceAndExpect(VALID_STUB) {
            status {
                isOk()
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
    fun `should return 400 on device registration with existing name`() {
        registerDeviceAndExpect(INVALID_STUB) {
            status {
                isBadRequest()
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