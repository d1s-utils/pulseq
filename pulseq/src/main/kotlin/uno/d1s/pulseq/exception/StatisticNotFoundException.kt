package uno.d1s.pulseq.exception

import uno.d1s.pulseq.constant.error.ErrorConstants

class StatisticNotFoundException(override val message: String = ErrorConstants.STATISTICS_NOT_FOUND) :
    RuntimeException(message)