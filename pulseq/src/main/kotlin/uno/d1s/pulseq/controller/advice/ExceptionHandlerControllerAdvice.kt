package uno.d1s.pulseq.controller.advice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uno.d1s.pulseq.exception.AbstractHttpStatusException
import uno.d1s.pulseq.util.HttpServletResponseUtil
import javax.servlet.http.HttpServletResponse


@RestControllerAdvice
class ExceptionHandlerControllerAdvice {

    @Autowired
    private lateinit var httpServletResponseUtil: HttpServletResponseUtil

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception, response: HttpServletResponse) {
        httpServletResponseUtil.sendErrorDto(response) {
            if (ex is AbstractHttpStatusException) {
                status = ex.status.value()
            }

            message = ex.message!!
        }
    }
}