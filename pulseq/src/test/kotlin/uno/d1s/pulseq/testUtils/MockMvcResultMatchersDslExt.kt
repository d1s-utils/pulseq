package uno.d1s.pulseq.util

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

internal fun MockMvcResultMatchersDsl.expectJsonContentType() {
    this.content {
        contentType(MediaType.APPLICATION_JSON)
    }
}