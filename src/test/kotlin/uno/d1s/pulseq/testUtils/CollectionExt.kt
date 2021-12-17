package uno.d1s.pulseq.testUtils

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

fun <T> List<T>.toPage(): Page<T> =
    PageImpl(this, PageRequest.of(0, Int.MAX_VALUE), this.size.toLong())
