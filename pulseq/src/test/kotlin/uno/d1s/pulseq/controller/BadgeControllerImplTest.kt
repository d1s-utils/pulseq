package uno.d1s.pulseq.controller

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
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.controller.impl.BadgeControllerImpl
import uno.d1s.pulseq.core.constant.mapping.BadgeMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.service.BadgeService
import uno.d1s.pulseq.testUtils.VALID_STUB

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [BadgeControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [BadgeControllerImpl::class])
internal class BadgeControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var badgeService: BadgeService

    @BeforeEach
    fun setup() {
        every {
            badgeService.createBadge(VALID_STUB, any(), any(), any(), any())
        }.returns(byteArrayOf())
    }

    @Test
    fun `should return the badge on getBadge and 200`() {
        mockMvc.get(BadgeMappingConstants.GET_BADGE.replacePathPlaceholder("metricId", VALID_STUB)) {
            param("color", "red")
            param("title", "redefined title")
        }.andExpect {
            status {
                isOk()
            }

            content {
                contentType("image/svg+xml")
            }
        }

        verify {
            badgeService.createBadge(VALID_STUB, any(), any(), any(), any())
        }
    }
}