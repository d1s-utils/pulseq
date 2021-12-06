package uno.d1s.pulseq.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.dto.ErrorDto

@SpringBootTest
@ContextConfiguration(classes = [HttpServletResponseUtil::class, JacksonAutoConfiguration::class])
class HttpServletResponseUtilTest {

    @Autowired
    private lateinit var httpServletResponseUtil: HttpServletResponseUtil

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should build valid response`() {
        val response = MockHttpServletResponse()
        httpServletResponseUtil.sendErrorDto(response) {
            status = HttpStatus.OK.value()
            message = "It's ok."
        }

        Assertions.assertEquals(HttpStatus.OK.value(), response.status)
        Assertions.assertEquals(
            "It's ok.", objectMapper.readValue(response.contentAsString, ErrorDto::class.java).message
        )
    }
}