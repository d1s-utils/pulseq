/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

@Suppress("unused")
class MetricNotFoundException(override val message: String = ErrorConstants.METRIC_NOT_FOUND) :
    AbstractHttpStatusException(HttpStatus.NOT_FOUND, message)