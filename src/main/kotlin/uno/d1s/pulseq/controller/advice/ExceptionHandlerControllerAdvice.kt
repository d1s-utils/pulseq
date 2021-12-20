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

    @ExceptionHandler
    fun handleAbstractHttpStatusException(ex: AbstractHttpStatusException, response: HttpServletResponse) {
        httpServletResponseUtil.sendErrorDto(response) {
            error = ex.message!!
            status = ex.status.value()
        }
    }
}