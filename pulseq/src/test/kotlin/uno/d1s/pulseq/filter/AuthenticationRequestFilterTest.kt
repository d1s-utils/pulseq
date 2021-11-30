package uno.d1s.pulseq.filter

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.service.AuthenticationService
import uno.d1s.pulseq.util.INVALID_STUB
import uno.d1s.pulseq.util.VALID_STUB

@SpringBootTest
@ContextConfiguration(classes = [AuthenticationRequestFilter::class])
internal class AuthenticationRequestFilterTest {

    @Autowired
    private lateinit var authenticationRequestFilter: AuthenticationRequestFilter

    @MockkBean
    private lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setup() {
        every {
            authenticationService.isSecuredPath(any())
        } returns true

        every {
            authenticationService.validateSecret(VALID_STUB)
        } returns true

        every {
            authenticationService.validateSecret(INVALID_STUB)
        } returns false
    }

    @Test
    fun `filter should proceed on valid auth data provided`() {
        Assertions.assertEquals(HttpStatus.OK.value(), doFilter {
            addHeader("Authorization", VALID_STUB)
        }.status)
    }

    @Test
    fun `filter should not proceed on invalid auth data provided`() {
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), doFilter {
            addHeader("Authorization", INVALID_STUB)
        }.status)
    }

    @Test
    fun `filter should not proceed when auth data is not present`() {
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), doFilter {}.status)
    }

    private fun doFilter(mockConfiguration: MockHttpServletRequest.() -> Unit): MockHttpServletResponse {
        val response = MockHttpServletResponse()

        authenticationRequestFilter.doFilter(
            MockHttpServletRequest().apply(mockConfiguration), response, MockFilterChain()
        )

        return response
    }
}