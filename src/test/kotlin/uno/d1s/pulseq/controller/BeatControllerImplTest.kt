/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.*
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.controller.impl.BeatControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.util.replacePathPlaceholder

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [BeatControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [BeatControllerImpl::class])
internal class BeatControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var beatService: BeatService

    @MockkBean
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @MockkBean
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    @BeforeEach
    fun setup() {
        paginationConfigurationProperties.setupTestStub()

        every {
            beatService.findBeatById(VALID_STUB)
        } returns testBeat

        every {
            beatService.registerNewBeatWithSourceIdentify(VALID_STUB)
        } returns testBeat

        every {
            beatService.findAllBeats()
        } returns testBeats

        every {
            beatService.findLastBeat()
        } returns testBeat

        justRun {
            beatService.deleteBeat(VALID_STUB)
        }

        every {
            beatDtoConverter.convertToDto(testBeat)
        } returns testBeatDto

        every {
            beatDtoConverter.convertToDtoList(testBeats)
        } returns testBeatsDto
    }

    @Test
    fun `should return 200 and valid beat on getting beat by id`() {
        mockMvc.get(BeatMappingConstants.GET_BEAT_BY_ID.replacePathPlaceholder("id", VALID_STUB)).andExpect {
            status {
                isOk()
            }

            expectBeatDto()

            expectJsonContentType()
        }

        verify {
            beatService.findBeatById(VALID_STUB)
        }

        verifyBeatConversion()
    }

    @Test
    fun `should return 201 and valid beat on beat registration`() {
        mockMvc.post(BeatMappingConstants.BASE) {
            header("Source", VALID_STUB)
        }.andExpect {
            status {
                isCreated()
            }

            expectBeatDto()

            expectJsonContentType()
        }

        verify {
            beatService.registerNewBeatWithSourceIdentify(VALID_STUB)
        }

        verifyBeatConversion()
    }

    @Test
    fun `should return 200 and valid list on getting beats`() {
        mockMvc.get(BeatMappingConstants.GET_BEATS).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testBeatsDto.toPage()))
            }

            expectJsonContentType()
        }

        verify {
            beatService.findAllBeats()
        }

        verifyBeatsConversion()
    }

    @Test
    fun `should return 200 and valid beat on getting last beat`() {
        mockMvc.get(BeatMappingConstants.LAST_BEAT).andExpect {
            status {
                isOk()
            }

            expectBeatDto()

            expectJsonContentType()
        }

        verify {
            beatService.findLastBeat()
        }

        verifyBeatConversion()
    }

    @Test
    fun `should return 204 on beat deletion`() {
        mockMvc.delete(BeatMappingConstants.GET_BEAT_BY_ID.replacePathPlaceholder("id", VALID_STUB)).andExpect {
            status {
                isNoContent()
            }

            content {
                string("")
            }
        }

        verify {
            beatService.deleteBeat(VALID_STUB)
        }
    }

    private fun MockMvcResultMatchersDsl.expectBeatDto() {
        content {
            json(objectMapper.writeValueAsString(testBeatDto))
        }
    }

    private fun verifyBeatConversion() {
        verify {
            beatDtoConverter.convertToDto(testBeat)
        }
    }

    private fun verifyBeatsConversion() {
        verify {
            beatDtoConverter.convertToDtoList(testBeats)
        }
    }
}