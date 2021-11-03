package uno.d1s.pulseq.controller.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import uno.d1s.pulseq.exception.BeatNotFoundException
import uno.d1s.pulseq.exception.DeviceNotFoundException
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.exception.StatisticNotFoundException


@ControllerAdvice
class ExceptionHandlerControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        BeatNotFoundException::class,
        DeviceNotFoundException::class,
        NoBeatsReceivedException::class,
        StatisticNotFoundException::class
    )
    protected fun handleConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return super.handleExceptionInternal(
            ex, ex.message, HttpHeaders(), HttpStatus.CONFLICT, request
        )
    }
}