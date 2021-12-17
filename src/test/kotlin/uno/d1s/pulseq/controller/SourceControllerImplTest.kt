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
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.constant.mapping.SourceMappingConstants
import uno.d1s.pulseq.controller.impl.SourceControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.source.SourceDto
import uno.d1s.pulseq.dto.source.SourcePatchDto
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.byAll
import uno.d1s.pulseq.testUtils.*
import uno.d1s.pulseq.util.replacePathPlaceholder

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [SourceControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [SourceControllerImpl::class])
internal class SourceControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var sourceService: SourceService

    @MockkBean
    private lateinit var sourceDtoConverter: DtoConverter<Source, SourceDto>

    @MockkBean
    private lateinit var sourcePatchDtoConverter: DtoConverter<Source, SourcePatchDto>

    @MockkBean
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @MockkBean
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    @BeforeEach
    fun setup() {
        paginationConfigurationProperties.setupTestStub()

        every {
            sourceService.findAllRegisteredSources()
        } returns testSources

        every {
            sourceService.findSource(byAll(VALID_STUB))
        } returns testSource

        every {
            sourceService.registerNewSource(any())
        } returns testSource

        every {
            sourceService.findSourceBeats(byAll(VALID_STUB))
        } returns testBeats

        every {
            sourceService.updateSource(byAll(VALID_STUB), testSourceUpdate)
        } returns testSourceUpdate

        every {
            sourceDtoConverter.convertToDto(testSource)
        } returns testSourceDto

        every {
            sourceDtoConverter.convertToDto(testSourceUpdate)
        } returns testSourceUpdateDto

        every {
            sourceDtoConverter.convertToDtoList(testSources)
        } returns testSourcesDto

        every {
            sourcePatchDtoConverter.convertToDomain(testSourcePatchDto)
        } returns testSourceUpdate

        every {
            beatDtoConverter.convertToDtoList(testBeats)
        } returns testBeatsDto
    }

    @Test
    fun `should return 200 and valid list on getting all sources`() {
        mockMvc.get(SourceMappingConstants.GET_ALL_SOURCES).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testSourcesDto.toPage()))
            }

            expectJsonContentType()
        }

        verify {
            sourceService.findAllRegisteredSources()
        }

        verify {
            sourceDtoConverter.convertToDtoList(testSources)
        }
    }

    @Test
    fun `should return 200 and valid source on getting source by identify`() {
        mockMvc.get(SourceMappingConstants.GET_SOURCE_BY_IDENTIFY.replaceIdentify())
            .andExpect {
                status {
                    isOk()
                }

                expectSourceDto()

                expectJsonContentType()
            }

        verify {
            sourceService.findSource(byAll(VALID_STUB))
        }

        verifySourceConversion()
    }

    @Test
    fun `should return 201 and valid source on source registration`() {
        mockMvc.post(SourceMappingConstants.REGISTER_SOURCE) {
            content = objectMapper.writeValueAsString(testSourcePatchDto)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isCreated()
            }

            expectSourceDto()

            expectJsonContentType()
        }

        verify {
            sourceService.registerNewSource(any())
        }

        verifySourceConversion()
    }

    @Test
    fun `should return 200 and valid list on getting beats by source identify`() {
        mockMvc.get(SourceMappingConstants.GET_BEATS.replaceIdentify()).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testBeatsDto.toPage()))
            }

            expectJsonContentType()
        }

        verify {
            sourceService.findSourceBeats(byAll(VALID_STUB))
        }

        verify {
            beatDtoConverter.convertToDtoList(testBeats)
        }
    }

    @Test
    fun `should return 202 and patch the source`() {
        mockMvc.patch(SourceMappingConstants.GET_SOURCE_BY_IDENTIFY.replaceIdentify()) {
            content = objectMapper.writeValueAsString(testSourcePatchDto)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status {
                isAccepted()
            }

            expectJsonContentType()

            content {
                json(objectMapper.writeValueAsString(testSourceUpdateDto))
            }
        }

        verify {
            sourceService.updateSource(byAll(VALID_STUB), testSourceUpdate)
        }

        verify {
            sourcePatchDtoConverter.convertToDomain(testSourcePatchDto)
        }
    }

    private fun MockMvcResultMatchersDsl.expectSourceDto() {
        content {
            json(objectMapper.writeValueAsString(testSourceDto))
        }
    }

    private fun verifySourceConversion() {
        verify {
            sourceDtoConverter.convertToDto(testSource)
        }
    }

    private fun String.replaceIdentify() =
        this.replacePathPlaceholder("identify", VALID_STUB)
}