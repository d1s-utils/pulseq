package uno.d1s.pulseq.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import uno.d1s.pulseq.exception.BeatNotFoundException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(BeatNotFoundException::class, DeviceNotFoundException::class, NoBeatsReceivedException::class)
    fun handleNotFound(exception: Exception, response: HttpServletResponse) {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.message)
    }
}