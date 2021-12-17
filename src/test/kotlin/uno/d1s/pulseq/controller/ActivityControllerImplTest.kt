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
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.service.ActivityService
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
    private lateinit var activityService: ActivityService

    @MockkBean
    private lateinit var timeSpanDtoConverter: DtoConverter<TimeSpan, TimeSpanDto>

    @MockkBean
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    @BeforeEach
    fun setup() {
        paginationConfigurationProperties.setupTestStub()

        every {
            activityService.getAllTimeSpans()
        } returns testTimeSpans

        every {
            activityService.getLongestTimeSpan()
        } returns testTimeSpan

        every {
            activityService.getCurrentTimeSpan()
        } returns testTimeSpan

        every {
            activityService.getLastRegisteredTimeSpan()
        } returns testTimeSpan

        every {
            timeSpanDtoConverter.convertToDto(testTimeSpan)
        } returns testTimeSpanDto

        every {
            timeSpanDtoConverter.convertToDtoList(testTimeSpans)
        } returns testTimeSpansDto
    }

    @Test
    fun `should return 200 and all time spans`() {
        mockMvc.get(ActivityMappingConstants.GET_TIMESPANS).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(testTimeSpansDto.toPage()))
            }

            expectJsonContentType()
        }

        verify {
            activityService.getAllTimeSpans()
        }
    }

    @Test
    fun `should return 200 and longest time span`() {
        verifyCommonTimeSpanResponse(ActivityMappingConstants.GET_LONGEST_TIME_SPAN)

        verify {
            activityService.getLongestTimeSpan()
        }
    }

    @Test
    fun `should return 200 and current time span`() {
        verifyCommonTimeSpanResponse(ActivityMappingConstants.GET_CURRENT_TIMESPAN)

        verify {
            activityService.getCurrentTimeSpan()
        }
    }

    @Test
    fun `should return 200 and last registered time span`() {
        verifyCommonTimeSpanResponse(ActivityMappingConstants.GET_LAST_TIMESPAN)

        verify {
            activityService.getLastRegisteredTimeSpan()
        }
    }

    private fun MockMvcResultMatchersDsl.expectTimeSpan() {
        content {
            json(objectMapper.writeValueAsString(testTimeSpanDto))
        }
    }

    private fun verifyCommonTimeSpanResponse(url: String) {
        mockMvc.get(url).andExpect {
            status {
                isOk()
            }

            expectTimeSpan()
            expectJsonContentType()
        }
    }
}