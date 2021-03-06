/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.testUtils

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

internal fun MockMvcResultMatchersDsl.expectJsonContentType() {
    this.content {
        contentType(MediaType.APPLICATION_JSON)
    }
}