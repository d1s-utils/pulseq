/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class TimeSpansNotAvailableException(
    override val message: String = ErrorConstants.TIME_SPANS_NOT_AVAILABLE
) : AbstractHttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message)