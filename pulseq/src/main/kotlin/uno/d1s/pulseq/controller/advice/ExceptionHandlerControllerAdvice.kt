package uno.d1s.pulseq.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class ExceptionHandlerControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        Exception::class
    )
    fun handleConflict(ex: RuntimeException, response: HttpServletResponse) {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.message)
    }
}