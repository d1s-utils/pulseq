/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

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
internal class ExceptionHandlerControllerAdviceTest {

    @Autowired
    private lateinit var exceptionHandlerControllerAdvice: ExceptionHandlerControllerAdvice

    @MockkBean
    private lateinit var httpServletResponseUtil: HttpServletResponseUtil

    private val response = MockHttpServletResponse()
    private val expectedStatus = HttpStatus.NOT_FOUND
    private val expectedUnauthorizedStatus = HttpStatus.FORBIDDEN

    @BeforeEach
    fun setup() {
        every {
            httpServletResponseUtil.sendErrorDto(response, any())
        } answers {
            response.status = expectedStatus.value()
        } andThen {
            response.status = expectedUnauthorizedStatus.value()
        }
    }

    @Test
    fun `should return valid response on error`() {
        val exception = object : AbstractHttpStatusException(expectedStatus, VALID_STUB) {}

        assertDoesNotThrow {
            exceptionHandlerControllerAdvice.handleAbstractHttpStatusException(exception, response)
        }

        Assertions.assertEquals(expectedStatus.value(), response.status)

        verify {
            httpServletResponseUtil.sendErrorDto(response, any())
        }
    }
}