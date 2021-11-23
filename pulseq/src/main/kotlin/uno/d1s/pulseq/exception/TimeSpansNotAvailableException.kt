package uno.d1s.pulseq.exception

import uno.d1s.pulseq.constant.error.ErrorConstants

class TimeSpansNotAvailableException(
    override val message: String = ErrorConstants.TIME_SPANS_NOT_AVAILABLE
) : RuntimeException(message)