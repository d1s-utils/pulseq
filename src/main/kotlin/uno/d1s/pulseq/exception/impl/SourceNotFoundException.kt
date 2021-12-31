/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class SourceNotFoundException(override val message: String = ErrorConstants.SOURCE_NOT_FOUND) :
    AbstractHttpStatusException(HttpStatus.NOT_FOUND, message)