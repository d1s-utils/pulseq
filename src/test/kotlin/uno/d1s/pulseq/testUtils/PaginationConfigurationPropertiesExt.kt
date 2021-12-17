package uno.d1s.pulseq.testUtils

import io.mockk.every
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties

fun PaginationConfigurationProperties.setupTestStub() {
    every {
        this@setupTestStub.defaultPageSize
    } returns Int.MAX_VALUE
}