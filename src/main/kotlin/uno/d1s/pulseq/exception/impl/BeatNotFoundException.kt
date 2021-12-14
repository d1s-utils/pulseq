package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class BeatNotFoundException(override val message: String = ErrorConstants.BEAT_NOT_FOUND) :
    AbstractHttpStatusException(HttpStatus.NOT_FOUND, message)