package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.exception.AbstractHttpStatusException

object InvalidImageUrlException :
    AbstractHttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The passed image URL is invalid or too large.")