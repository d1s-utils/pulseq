package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import uno.d1s.pulseq.INVALID_ID
import uno.d1s.pulseq.VALID_ID
import uno.d1s.pulseq.controller.advice.ExceptionHandlerControllerAdvice
import uno.d1s.pulseq.controller.impl.BeatControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.core.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.exception.BeatNotFoundException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.testBeat
import uno.d1s.pulseq.testBeatDto

@WebMvcTest(useDefaultFilters = false, controllers = [BeatControllerImpl::class])
@ContextConfiguration(classes = [BeatControllerImpl::class, ExceptionHandlerControllerAdvice::class])
class BeatControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var beatService: BeatService

    @MockkBean
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @BeforeEach
    fun setup() {
        every {
            beatService.findBeatById(VALID_ID)
        } returns testBeat

        every {
            beatService.findBeatById(INVALID_ID)
        } throws BeatNotFoundException()

        every {
            beatService.registerNewBeatWithDeviceIdentify(any())
        } returns testBeat

        every {
            beatService.findAllBeatsByDeviceIdentify(VALID_ID)
        } returns listOf()

        every {
            beatService.findAllBeatsByDeviceIdentify(INVALID_ID)
        } throws DeviceNotFoundException()

        every {
            beatService.findAllBeats()
        } returns listOf()

        every {
            beatService.findLastBeat()
        } returns testBeat

        every {
            beatDtoConverter.convertToDto(testBeat)
        } returns testBeatDto

        every {
            beatDtoConverter.convertToDtoList(any())
        } returns listOf()
    }

    @Test
    fun `should return 200 and valid beat on getting beat by id`() {
        getBeatByIdAndExpect(VALID_ID) {
            status {
                isOk()
            }

            expectBeatDto()
        }
    }

    @Test
    fun `should return 400 on getting beat with invalid id`() {
        getBeatByIdAndExpect(INVALID_ID) {
            status {
                isBadRequest()
            }
        }
    }

    @Test
    fun `should return 200 and valid beat on beat registration`() {
        mockMvc.post(BeatMappingConstants.BASE) {
            header("Device", "test_device")
        }.andExpect {
            status {
                isOk()
            }

            expectBeatDto()
        }
    }

    @Test
    fun `should return 200 and valid list on getting beats by device identify`() {
        getBeatsByDeviceIdentifyAndExpect(VALID_ID) {
            status {
                isOk()
            }

            expectBeatDtoList()
        }
    }

    @Test
    fun `should return 400 on getting beats by invalid device identify`() {
        getBeatsByDeviceIdentifyAndExpect(INVALID_ID) {
            status {
                isBadRequest()
            }
        }
    }

    @Test
    fun `should return 200 and valid list on getting beats`() {
        mockMvc.get(BeatMappingConstants.GET_BEATS).andExpect {
            status {
                isOk()
            }

            expectBeatDtoList()
        }
    }

    @Test
    fun `should return 200 and valid beat on getting last beat`() {
        mockMvc.get(BeatMappingConstants.LAST_BEAT).andExpect {
            status {
                isOk()
            }

            expectBeatDto()
        }
    }

    private fun getBeatByIdAndExpect(id: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(BeatMappingConstants.GET_BEAT_BY_ID.replacePathPlaceholder("id", id)).andExpect(block)
    }

    private fun getBeatsByDeviceIdentifyAndExpect(id: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(BeatMappingConstants.GET_BEATS_BY_DEVICE_IDENTIFY.replacePathPlaceholder("identify", id))
            .andExpect(block)
    }

    private fun MockMvcResultMatchersDsl.expectBeatDto() {
        content {
            json(objectMapper.writeValueAsString(testBeatDto))
        }
    }

    private fun MockMvcResultMatchersDsl.expectBeatDtoList() {
        content {
            json(objectMapper.writeValueAsString(listOf<BeatDto>()))
        }
    }
}