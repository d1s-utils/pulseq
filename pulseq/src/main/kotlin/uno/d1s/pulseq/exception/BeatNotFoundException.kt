package uno.d1s.pulseq.exception

import uno.d1s.pulseq.constant.error.ErrorConstants

class BeatNotFoundException(override val message: String = ErrorConstants.BEAT_NOT_FOUND) : RuntimeException(message)