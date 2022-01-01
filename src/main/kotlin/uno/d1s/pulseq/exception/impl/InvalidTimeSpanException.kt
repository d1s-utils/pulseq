/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class InvalidTimeSpanException : AbstractHttpStatusException(HttpStatus.BAD_REQUEST, ErrorConstants.INVALID_TIME_SPAN)