/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class SourceAlreadyExistsException(message: String = ErrorConstants.SOURCE_ALREADY_EXISTS) :
    AbstractHttpStatusException(HttpStatus.CONFLICT, message)