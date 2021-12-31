/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util.http

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.dto.ErrorDto
import javax.servlet.http.HttpServletResponse

@Component
class HttpServletResponseUtil {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun sendErrorDto(response: HttpServletResponse, error: ErrorDto.() -> Unit) {
        val errorDto = ErrorDto().apply(error)

        response.apply {
            contentType = "application/json"
            status = errorDto.status
            writer.println(objectMapper.writeValueAsString(errorDto))
        }
    }
}
