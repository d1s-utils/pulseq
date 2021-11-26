package uno.d1s.pulseq.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.VALID_ID
import uno.d1s.pulseq.controller.impl.BeatControllerImpl
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.core.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.testBeat
import uno.d1s.pulseq.testBeatDto

@ContextConfiguration(classes = [BeatControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [BeatControllerImpl::class])
class BeatControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

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
            beatDtoConverter.convertToDto(testBeat)
        } returns testBeatDto
    }

    @Test
    fun `should return 200 on beat registration and valid beat`() {
        mockMvc.get(BeatMappingConstants.GET_BEAT_BY_ID.replacePathPlaceholder("id", VALID_ID)).andExpect {
            status {
                isOk()
            }
        }
    }
}