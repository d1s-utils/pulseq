/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception

import org.springframework.http.HttpStatus

interface HttpStatusException {

    val status: HttpStatus
}