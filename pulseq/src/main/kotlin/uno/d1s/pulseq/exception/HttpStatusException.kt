package uno.d1s.pulseq.exception

import org.springframework.http.HttpStatus

interface HttpStatusException {

    val status: HttpStatus
}