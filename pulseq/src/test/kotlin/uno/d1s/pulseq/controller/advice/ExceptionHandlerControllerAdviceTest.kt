package uno.d1s.pulseq.controller.advice

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.exception.AbstractHttpStatusException
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.util.HttpServletResponseUtil

@SpringBootTest
@ContextConfiguration(classes = [ExceptionHandlerControllerAdvice::class])
class ExceptionHandlerControllerAdviceTest {

    @Autowired
    private lateinit var exceptionHandlerControllerAdvice: ExceptionHandlerControllerAdvice

    @MockkBean
    private lateinit var httpServletResponseUtil: HttpServletResponseUtil

    private val response = MockHttpServletResponse()
    private val expectedStatus = 404

    @BeforeEach
    fun setup() {
        every {
            httpServletResponseUtil.sendErrorDto(response, any())
        } answers {
            response.status = expectedStatus
        }
    }

    @Test
    fun `should return valid response on error`() {
        val exception = object : AbstractHttpStatusException(HttpStatus.valueOf(expectedStatus), VALID_STUB) {}

        assertDoesNotThrow {
            exceptionHandlerControllerAdvice.handle(exception, response)
        }

        Assertions.assertEquals(expectedStatus, response.status)

        verify {
            httpServletResponseUtil.sendErrorDto(response, any())
        }
    }
}