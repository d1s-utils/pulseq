/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.util.currentRequest
import java.time.Instant

data class ErrorDto(
    var timestamp: Instant = Instant.now(),
    var status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    var error: String = "Something went wrong.",
    var path: String? = currentRequest.pathInfo
)