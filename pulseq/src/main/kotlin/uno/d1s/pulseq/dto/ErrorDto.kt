package uno.d1s.pulseq.dto

import org.springframework.http.HttpStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.Instant

class ErrorDto(
    var status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    var message: String = "Something went wrong.",
    var timestamp: Instant = Instant.now(),
    var url: String = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()
)