/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

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
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.constant.mapping.ActivityMappingConstants
import uno.d1s.pulseq.controller.impl.ActivityControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.dto.beat.BeatIntervalDto
import uno.d1s.pulseq.service.IntervalService
import uno.d1s.pulseq.testUtils.*

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [ActivityControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [ActivityControllerImpl::class])
internal class ActivityControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var intervalService: IntervalService

    @MockkBean
    private lateinit var beatBeatIntervalDtoConverter: DtoConverter<BeatInterval, BeatIntervalDto>

    @MockkBean
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    @BeforeEach
    fun setup() {
        paginationConfigurationProperties.setupTestStub()

        every {
            intervalService.findAllIntervals()
        } returns testDurations

        every {
            intervalService.findLongestInterval()
        } returns testBeatInterval

        every {
            intervalService.findCurrentInterval()
        } returns testBeatInterval

        every {
            intervalService.getLastRegisteredDuration()
        } returns testBeatInterval

        every {
            beatBeatIntervalDtoConverter.convertToDto(testBeatInterval)
        } returns testBeatIntervalDto

        every {
            beatBeatIntervalDtoConverter.convertToDtoList(testDurations)
        } returns testDurationsDto
    }

    @Test
    fun `should return 200 and all time spans`() {
        mockMvc.get(ActivityMappingConstants.GET_DURATIONS).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testDurationsDto.toPage()))
            }

            expectJsonContentType()
        }

        verify {
            intervalService.findAllIntervals()
        }
    }

    @Test
    fun `should return 200 and longest time span`() {
        verifyCommonDurationResponse(ActivityMappingConstants.GET_LONGEST_DURATION)

        verify {
            intervalService.findLongestInterval()
        }
    }

    @Test
    fun `should return 200 and current time span`() {
        verifyCommonDurationResponse(ActivityMappingConstants.GET_CURRENT_DURATION)

        verify {
            intervalService.findCurrentInterval()
        }
    }

    @Test
    fun `should return 200 and last registered time span`() {
        verifyCommonDurationResponse(ActivityMappingConstants.GET_LAST_DURATION)

        verify {
            intervalService.getLastRegisteredDuration()
        }
    }

    private fun MockMvcResultMatchersDsl.expectDuration() {
        content {
            json(objectMapper.writeValueAsString(testBeatIntervalDto))
        }
    }

    private fun verifyCommonDurationResponse(url: String) {
        mockMvc.get(url).andExpect {
            status {
                isOk()
            }

            expectDuration()
            expectJsonContentType()
        }
    }
}