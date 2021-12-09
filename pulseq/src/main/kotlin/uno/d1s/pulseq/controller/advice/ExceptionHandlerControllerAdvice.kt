package uno.d1s.pulseq.controller.advice

import org.apache.logging.log4j.LogManager
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

    private val logger = LogManager.getLogger()

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception, response: HttpServletResponse) {
        httpServletResponseUtil.sendErrorDto(response) {
            if (ex is AbstractHttpStatusException) {
                status = ex.status.value()
            } else {
                logger.error(ex)
            }

            message = (ex::class.simpleName ?: "Anonymous Object") + if (ex.message != null) {
                ": ${ex.message}"
            } else {
                ""
            }
        }
    }
}