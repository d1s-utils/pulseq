package uno.d1s.pulseq.controller

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
import uno.d1s.pulseq.INVALID_ID
import uno.d1s.pulseq.VALID_ID
import uno.d1s.pulseq.controller.advice.ExceptionHandlerControllerAdvice
import uno.d1s.pulseq.controller.impl.BadgeControllerImpl
import uno.d1s.pulseq.core.constant.mapping.BadgeMappingConstants
import uno.d1s.pulseq.core.util.replacePathPlaceholder
import uno.d1s.pulseq.exception.StatisticNotFoundException
import uno.d1s.pulseq.service.BadgeService

@WebMvcTest(useDefaultFilters = false, controllers = [BadgeControllerImpl::class])
@ContextConfiguration(classes = [BadgeControllerImpl::class, ExceptionHandlerControllerAdvice::class])
class BadgeControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var badgeService: BadgeService

    @BeforeEach
    fun setup() {
        every {
            badgeService.createBadge(VALID_ID, any(), any(), any(), any())
        }.returns(byteArrayOf())

        every {
            badgeService.createBadge(INVALID_ID, any(), any(), any(), any())
        } throws StatisticNotFoundException()
    }

    @Test
    fun `should return the badge on getBadge and 200`() {
        getAndExpect(VALID_ID) {
            content {
                contentType("image/svg+xml")
            }

            status {
                isOk()
            }
        }
    }

    @Test
    fun `should return 400 on invalid statistic id`() {
        getAndExpect(INVALID_ID) {
            status {
                isBadRequest()
            }
        }
    }

    private fun getAndExpect(statisticId: String, block: MockMvcResultMatchersDsl.() -> Unit) {
        mockMvc.get(BadgeMappingConstants.GET_BADGE.replacePathPlaceholder("statisticId", statisticId)) {
            param("color", "red")
            param("title", "redefined title")
        }.andExpect(block)
    }
}